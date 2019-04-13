from corpus import Corpus
from collections import Counter
import re


class TrainingCorpus(Corpus):
    ''' Class based on corpus with few usefull functions '''

    # Class constants
    SPAM_CLASS = "SPAM"
    HAM_CLASS = "OK"

    def __init__(self, path_to_training_corpus):
        '''
        Constructor
        :param path_to_training_corpus: pretty selfexplaining
        '''

        self.path = path_to_training_corpus
        self.classification_dict = self.get_class_dict()

    def get_class_dict(self):
        '''
        Helps me with other stuff
        :return: dictionary of filename and its class
        '''

        class_dict = {}
        with open(self.path + "/" + self.TRUTHFILE, mode='r', encoding="utf-8") as file:
            lines = file.readlines()

        for line in lines:
            linesplit = line.split()
            class_dict.update({linesplit[0] : linesplit[1]})

        return class_dict

    def return_spam_ham_count(self):
            spam = 0
            ham = 0
            for value in self.classification_dict:
                if self.classification_dict[value] == self.SPAM_CLASS:
                    spam += 1
                else:
                    ham += 1

            return spam, ham

    def get_class(self, filename):
        '''
        Returns class for given file
        :param filename: file for which we want to know class
        :return: right classification or None
        '''
        try:
            return self.classification_dict[filename]
        except KeyError:
            return None

    def get_spam_word_count_dict_and_avg(self):
        '''
        Function for spams
        :return: Tuple of word frequency and average word count
        '''

        return self.get_word_count_and_avg(self.spams())

    def get_ham_word_count_dict_and_avg(self):
        '''
            Function for hams
            :return: Tuple of word frequency and average word count
        '''

        return self.get_word_count_and_avg(self.hams())

    def get_word_count_and_avg(self, ham_or_spam):
        '''
        Counts word frequency and average word count
        :param ham_or_spam: correct generator
        :return: Tuple of word frequency and average word count
        '''

        # Prepare "global" variables
        final = Counter({})
        average_counts = []
        counter = 0

        # Iterate over emails with generator given
        for email in ham_or_spam:
            # Prepare things
            counts = {}
            counter += 1
            email_split = email[1].split("\n\n")
            email_split[1].lower()

            # Find words only 3-15 chars -> filters out html stuff and other bullshit
            match_pattern = re.findall(r'\b[a-z]{3,15}\b', email_split[1])

            # Adds word count to array
            average_counts.append(len(match_pattern))

            # Find count for every word
            for word in match_pattern:
                count = counts.get(word, 0)
                counts[word] = count + 1

            frequency_list = counts.keys()

            # Creates dict of frequencies
            dict = {}
            for words in frequency_list:
                dict.update({words: counts[words]})

            # Updates gobal counter
            c = Counter(dict)
            final += c

        # Calculate average word lenght
        avg = sum(average_counts)/len(average_counts)

        return final, avg

    def spams(self):
        '''
        Generator for spam emails
        :return: yields spam
        '''

        for email in self.emails():
            if self.get_class(email[0]) == self.SPAM_CLASS:
                yield email

    def hams(self):
        '''
            Generator for ham emails
            :return: yields ham
        '''

        for email in self.emails():
            if self.get_class(email[0]) == self.HAM_CLASS:
                yield email
