package mg.biblio.gestion_biblio.service;

import java.time.LocalDate;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mg.biblio.gestion_biblio.enums.TypeStatutPaiement;
import mg.biblio.gestion_biblio.exception.AbonnementExpireException;
import mg.biblio.gestion_biblio.exception.AdherentNonTrouveException;
import mg.biblio.gestion_biblio.exception.ExemplaireNonDisponibleException;
import mg.biblio.gestion_biblio.exception.PenaliteEnCoursException;
import mg.biblio.gestion_biblio.exception.PretException;
import mg.biblio.gestion_biblio.exception.QuotaLivreAtteintException;
import mg.biblio.gestion_biblio.model.Adherent;
import mg.biblio.gestion_biblio.model.Exemplaire;
import mg.biblio.gestion_biblio.model.ParametrePret;
import mg.biblio.gestion_biblio.model.Pret;
import mg.biblio.gestion_biblio.model.TypeAdherent;
import mg.biblio.gestion_biblio.repository.AbonnementRepository;
import mg.biblio.gestion_biblio.repository.AdherentRepository;
import mg.biblio.gestion_biblio.repository.ExemplaireRepository;
import mg.biblio.gestion_biblio.repository.ParametrePretRepository;
import mg.biblio.gestion_biblio.repository.PenaliteRepository;
import mg.biblio.gestion_biblio.repository.PretRepository;

@Service
public class PretService {

    private final PretRepository pretRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final AdherentRepository adherentRepository;
    private final PenaliteRepository penaliteRepository;
    private final AbonnementRepository abonnementRepository;
    private final ParametrePretRepository parametrePretRepository;

    public PretService(PretRepository pretRepository,
            ExemplaireRepository exemplaireRepository,
            AdherentRepository adherentRepository,
            PenaliteRepository penaliteRepository,
            AbonnementRepository abonnementRepository,
            ParametrePretRepository parametrePretRepository) {
        this.pretRepository = pretRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.adherentRepository = adherentRepository;
        this.penaliteRepository = penaliteRepository;
        this.abonnementRepository = abonnementRepository;
        this.parametrePretRepository = parametrePretRepository;
    }

    @Transactional
    public Pret emprunterLivre(Long idExemplaire) {
        // === Étape 1 : Récupérer les entités de base ===

        // Récupérer l'utilisateur connecté via le contexte de sécurité
        String emailUtilisateurConnecte = SecurityContextHolder.getContext().getAuthentication().getName();
        Adherent adherent = adherentRepository.findByUtilisateurEmail(emailUtilisateurConnecte)
                .orElseThrow(
                        () -> new AdherentNonTrouveException("L'utilisateur connecté n'est pas un adhérent valide."));

        // Récupérer l'exemplaire à emprunter
        Exemplaire exemplaire = exemplaireRepository.findById(idExemplaire)
                .orElseThrow(() -> new PretException("L'exemplaire avec l'ID " + idExemplaire + " n'existe pas."));

        // === Étape 2 : VÉRIFICATION DES RÈGLES DE GESTION ===

        // Règle 2.1: L'exemplaire doit être disponible
        if (!exemplaire.getDisponible()) {
            throw new ExemplaireNonDisponibleException("Cet exemplaire est déjà emprunté ou indisponible.");
        }

        // Règle 4.4: L'adhérent doit avoir un abonnement actif
        // CORRECTION 1 : La méthode du repository attendait la date actuelle en
        // paramètre.
        abonnementRepository.findActiveByAdherent(adherent.getId(), LocalDate.now())
                .orElseThrow(() -> new AbonnementExpireException(
                        "Votre abonnement est expiré ou inexistant. Veuillez le renouveler."));

        // Règle 4.3: L'adhérent ne doit pas avoir de pénalités impayées
        // CORRECTION 2 : La méthode du repository attend un type ENUM, pas une chaîne
        // de caractères.
        long penalitesEnAttente = penaliteRepository.countByAdherentAndStatut(adherent, TypeStatutPaiement.EN_ATTENTE);
        if (penalitesEnAttente > 0) {
            throw new PenaliteEnCoursException(
                    "Vous avez " + penalitesEnAttente
                            + " pénalité(s) impayée(s). Veuillez les régler avant d'emprunter.");
        }

        // Règle 4.1: Vérifier le quota de livres
        // CORRECTION 3 : Il faut récupérer le TypeAdherent à partir de l'entité
        // Adherent.
        TypeAdherent typeAdherent = adherent.getTypeAdherent();
        if (typeAdherent == null) {
            throw new PretException("Le type d'adhérent n'est pas défini. Impossible de vérifier les règles de prêt.");
        }

        ParametrePret parametrePret = parametrePretRepository.findByTypeAdherent(typeAdherent)
                .orElseThrow(() -> new PretException(
                        "Aucun paramètre de prêt n'est configuré pour le type d'adhérent : " + typeAdherent.getNom()));

        long livresDejaEmpruntes = pretRepository.countByAdherentAndDateRetourReelleIsNull(adherent);
        if (livresDejaEmpruntes >= parametrePret.getMaxLivres()) {
            throw new QuotaLivreAtteintException(
                    "Vous avez atteint votre quota maximum de " + parametrePret.getMaxLivres() + " livres empruntés.");
        }

        // Règle 4.2: (Optionnel) Vérifier les restrictions du livre (non implémenté
        // pour l'instant)

        // === Étape 3 : SI TOUT EST OK, PERSISTANCE DU PRÊT ===

        // Mettre à jour le statut de l'exemplaire
        exemplaire.setDisponible(false);
        exemplaireRepository.save(exemplaire);

        // Créer et configurer le nouvel objet Prêt
        Pret nouveauPret = new Pret();
        nouveauPret.setExemplaire(exemplaire);
        nouveauPret.setAdherent(adherent);
        nouveauPret.setDateEmprunt(LocalDate.now());
        nouveauPret.setDateRetourPrevue(LocalDate.now().plusDays(parametrePret.getDureeMaxPretJours()));
        // Vous pouvez initialiser d'autres champs ici si nécessaire (ex: état du prêt)

        // Sauvegarder le nouveau prêt dans la base de données
        return pretRepository.save(nouveauPret);
    }
}