package com.piotrpabich.talktactics.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserProfileBioGenerator {
    private static final String[] SENTENCES = {
            "Exploring the depths of English vocabulary one word at a time.",
            "Enthusiastic language learner passionate about expanding my English lexicon.",
            "Embarking on a linguistic journey to master the intricacies of the English language.",
            "Diving into the world of English words to broaden my linguistic horizons.",
            "Devoted to enriching my vocabulary with diverse English expressions.",
            "Immersing myself in the vibrant tapestry of the English language.",
            "Eager to discover new English words and their meanings.",
            "Venturing into the realm of English vocabulary to enhance my communication skills.",
            "Building my arsenal of English words for clearer and more effective communication.",
            "On a quest to unlock the treasure trove of the English language.",
            "Uncovering the nuances of English vocabulary to express myself more eloquently.",
            "Committed to mastering English idioms and phrases for natural language fluency.",
            "Absorbing the richness of English vocabulary to articulate thoughts with precision.",
            "Seeking to elevate my English language proficiency through continuous learning.",
            "Fascinated by the diversity of English words and their etymology.",
            "Engaged in a lifelong pursuit of English language proficiency.",
            "Captivated by the beauty and complexity of the English language.",
            "Exploring the vast expanse of English vocabulary to sharpen my linguistic skills.",
            "Enriching my linguistic repertoire with a plethora of English words.",
            "Determined to expand my English vocabulary for academic and professional success.",
            "Passionate about mastering English grammar and syntax.",
            "Driven by curiosity to learn new English words and phrases daily.",
            "Immersing myself in English literature to absorb new vocabulary effortlessly.",
            "Striving for fluency in English through dedicated vocabulary acquisition.",
            "Curating a collection of sophisticated English words for eloquent expression.",
            "Delving into the intricacies of English semantics and syntax.",
            "Embracing the challenge of learning English as a second language.",
            "Discovering the power of words in shaping meaningful English communication.",
            "Nurturing a love for English language learning through constant exposure.",
            "Embracing the beauty of linguistic diversity through English vocabulary.",
            "Committing to daily practice to enhance my English language skills.",
            "Infatuated with the richness and diversity of English vocabulary.",
            "Engaged in the pursuit of English language mastery with unwavering determination.",
            "Intrigued by the cultural nuances embedded within English words.",
            "Persistently expanding my English vocabulary for effective communication.",
            "Cultivating a deep appreciation for the nuances of English language.",
            "Motivated to excel in English language proficiency through dedicated study.",
            "Enamored by the elegance and expressiveness of English vocabulary.",
            "Curious explorer of the vast English lexicon, always seeking new words to learn.",
            "Passionate about unlocking the mysteries of English vocabulary.",
            "Fascinated by the evolution of English words over time.",
            "Eager learner striving to master English vocabulary and grammar.",
            "Determined to conquer the complexities of English language learning.",
            "Embracing the challenge of expanding my English vocabulary daily.",
            "Committed to honing my English language skills through continuous practice.",
            "Devoted student of English language and literature, eager to absorb new words.",
            "Enthralled by the beauty of English vocabulary and its expressive power.",
            "Striving to become fluent in English through diligent study and practice.",
            "Entranced by the sheer diversity of English words and their meanings.",
            "Inquisitive language enthusiast on a mission to master English vocabulary."
    };

    public static String generateBio(int sentencesCount) {
        List<String> selectedSentences = selectRandomSentences(sentencesCount);
        return String.join(" ", selectedSentences);
    }

    private static List<String> selectRandomSentences(int count) {
        List<String> selectedSentences = new ArrayList<>();
        Random random = new Random();

        while (selectedSentences.size() < count) {
            String randomSentence = SENTENCES[random.nextInt(SENTENCES.length)];
            if (!selectedSentences.contains(randomSentence)) {
                selectedSentences.add(randomSentence);
            }
        }

        return selectedSentences;
    }
}
