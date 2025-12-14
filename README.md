# Weight Tracker Pro

Une application Android moderne et complète pour suivre votre poids, votre composition corporelle et vos objectifs de santé.

## 🎯 Fonctionnalités Principales

### 1. **Suivi du Poids**
- Enregistrement quotidien du poids
- Historique complet des entrées
- Sélection de date personnalisée
- Notes pour chaque entrée
- Visualisation des tendances

### 2. **Métriques de Santé**
- Calcul automatique de l'IMC (Indice de Masse Corporelle)
- Suivi des mensurations (tour de taille, hanches, poitrine)
- Suivi de la masse musculaire et du pourcentage de graisse
- Catégorisation de l'IMC (Insuffisant, Normal, Surpoids, Obésité)

### 3. **Objectifs Personnalisés**
- Création d'objectifs de perte/gain de poids
- Suivi du progrès en temps réel
- Objectifs multiples simultanés
- Historique des objectifs complétés
- Visualisation du pourcentage de réalisation

### 4. **Journal Alimentaire**
- Enregistrement des aliments consommés
- Suivi des calories par repas
- Suivi des macronutriments (protéines, glucides, lipides)
- Classification par type de repas (petit-déjeuner, déjeuner, dîner, collation)
- Résumé nutritionnel quotidien

### 5. **Suivi de l'Activité Physique**
- Enregistrement des activités sportives
- Suivi de la durée et de l'intensité
- Estimation des calories brûlées
- Historique des activités

### 6. **Graphiques et Rapports**
- Graphiques de progression du poids
- Analyse des tendances
- Statistiques hebdomadaires/mensuelles/annuelles
- Rapports détaillés sur la composition corporelle
- Visualisation des macronutriments

### 7. **Notifications Motivantes**
- Rappels quotidiens pour enregistrer le poids
- Alertes de progression
- Notifications de réussite d'objectifs
- Messages motivants personnalisés

### 8. **Sécurité des Données**
- Chiffrement local des données sensibles
- Sauvegarde sécurisée
- Authentification optionnelle
- Respect de la vie privée

## 🏗️ Architecture

### Stack Technologique
- **Langage**: Java 11
- **Framework**: Android 14 (API 34)
- **Base de données**: Room Database (SQLite)
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI**: Material Design 3
- **Graphiques**: MPAndroidChart

### Structure du Projet

```
WeightTrackerProApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/weighttrackerapp/
│   │   │   │   ├── activities/          # Activities (MainActivity, SplashActivity)
│   │   │   │   ├── fragments/           # Fragments (Dashboard, Weight, Goals, Food, Analytics)
│   │   │   │   ├── viewmodels/          # ViewModels
│   │   │   │   ├── models/              # Modèles de données
│   │   │   │   ├── database/            # Room Database, DAOs
│   │   │   │   ├── adapters/            # RecyclerView Adapters
│   │   │   │   ├── repositories/        # Data repositories
│   │   │   │   ├── services/            # Services (Notifications, etc.)
│   │   │   │   └── utils/               # Utilitaires
│   │   │   ├── res/
│   │   │   │   ├── layout/              # Layouts XML
│   │   │   │   ├── drawable/            # Images et drawables
│   │   │   │   ├── values/              # Ressources (couleurs, strings)
│   │   │   │   ├── values-night/        # Ressources mode sombre
│   │   │   │   ├── menu/                # Menus
│   │   │   │   └── anim/                # Animations
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

## 🚀 Installation et Configuration

### Prérequis
- Android Studio 2023.1 ou supérieur
- JDK 11 ou supérieur
- Android SDK 34 (API Level 34)
- Gradle 8.2.0 ou supérieur

### Étapes d'Installation

1. **Cloner le repository**
   ```bash
   git clone <repository-url>
   cd WeightTrackerProApp
   ```

2. **Ouvrir dans Android Studio**
   - Ouvrir Android Studio
   - Sélectionner "Open an Existing Project"
   - Naviguer vers le dossier `WeightTrackerProApp`

3. **Synchroniser Gradle**
   - Android Studio synchronisera automatiquement les dépendances
   - Attendre que la synchronisation soit complète

4. **Configurer l'émulateur ou le device**
   - Créer un émulateur Android (API 24 minimum)
   - Ou connecter un device Android physique

5. **Compiler et exécuter**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```

## 📱 Utilisation

### Dashboard
- Vue d'ensemble de votre santé
- Poids actuel, IMC, calories du jour
- Progrès des objectifs actifs

### Suivi du Poids
- Enregistrer votre poids quotidien
- Consulter l'historique complet
- Ajouter des notes personnelles

### Objectifs
- Créer de nouveaux objectifs
- Suivre la progression
- Consulter les objectifs complétés

### Journal Alimentaire
- Enregistrer les repas
- Suivre les calories et macronutriments
- Voir le résumé nutritionnel quotidien

### Analytique
- Visualiser les graphiques de progression
- Consulter les statistiques détaillées
- Analyser les tendances

## 🔐 Sécurité

- **Chiffrement local**: Les données sensibles sont chiffrées localement
- **Authentification**: Support optionnel de l'authentification biométrique
- **Permissions**: Utilisation minimale des permissions système
- **Sauvegarde**: Sauvegarde sécurisée des données

## 🎨 Design

L'application utilise **Material Design 3** pour une expérience utilisateur moderne et cohérente.

### Palette de Couleurs
- **Primaire**: #6750A4 (Violet)
- **Secondaire**: #625B71 (Gris-violet)
- **Tertiaire**: #7D5260 (Rose)
- **Succès**: #4CAF50 (Vert)
- **Alerte**: #FF9800 (Orange)
- **Erreur**: #F44336 (Rouge)

### Thèmes
- Mode clair (par défaut)
- Mode sombre (automatique selon les paramètres système)

## 📊 Modèles de Données

### WeightEntry
- `id`: Identifiant unique
- `weight`: Poids en kg
- `date`: Date de l'enregistrement
- `notes`: Notes optionnelles

### Goal
- `id`: Identifiant unique
- `title`: Titre de l'objectif
- `description`: Description
- `targetWeight`: Poids cible
- `startWeight`: Poids de départ
- `startDate`: Date de début
- `endDate`: Date cible
- `status`: Statut (actif, complété, abandonné)

### FoodEntry
- `id`: Identifiant unique
- `foodName`: Nom de l'aliment
- `date`: Date de l'enregistrement
- `calories`: Calories
- `protein`: Protéines (g)
- `carbs`: Glucides (g)
- `fat`: Lipides (g)
- `mealType`: Type de repas

### Measurement
- `id`: Identifiant unique
- `date`: Date de la mesure
- `waist`: Tour de taille (cm)
- `hips`: Tour de hanches (cm)
- `chest`: Tour de poitrine (cm)
- `muscleMass`: Masse musculaire (%)
- `bodyFat`: Pourcentage de graisse (%)

## 🔄 Intégrations Futures

- **Appareils de fitness**: Intégration avec Fitbit, Apple Health, Google Fit
- **Réseaux sociaux**: Partage des progrès
- **Cloud Sync**: Synchronisation cloud des données
- **IA/ML**: Recommandations personnalisées
- **Backend API**: Synchronisation multi-device

## 📝 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👨‍💻 Auteur

Développé avec ❤️ pour aider les utilisateurs à atteindre leurs objectifs de santé.

## 📞 Support

Pour toute question ou problème, veuillez ouvrir une issue sur le repository.

---

**Version**: 1.0.0  
**Dernière mise à jour**: Décembre 2024
