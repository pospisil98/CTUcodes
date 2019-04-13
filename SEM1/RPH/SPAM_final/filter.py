from basefilter import BaseFilter
from corpus import Corpus
from trainingcorpus import TrainingCorpus
import utils
from collections import Counter
import re
import operator
from functools import reduce


class MyFilter(BaseFilter):
    ''' Some magic happens here '''

    # I'm classifiing messages by word frequency learned on training corpus

    def __init__(self):
        self.spam_word_count_dict = Counter({})
        self.ham_word_count_dict = Counter({})
        self.spam_word_count_avg = 0
        self.ham_word_count_avg = 0
        self.spam_count = 0
        self.ham_count = 0

    def train(self, train_corpus_dir):
        '''
        Trains my silly filter
        :param train_corpus_dir: path to train dir
        :return: None
        '''

        tc = TrainingCorpus(train_corpus_dir)
        tc.return_spam_ham_count()

        # Get word frequencies
        spam = tc.get_spam_word_count_dict_and_avg()
        ham = tc.get_ham_word_count_dict_and_avg()
        self.spam_word_count_dict = spam[0]
        self.ham_word_count_dict = ham[0]

        self.spam_word_count_avg = spam[1]
        self.ham_word_count_avg = ham[1]

        # Remove intersection of them from them
        # intersection = self.spam_word_count_dict & self.ham_word_count_dict
        # self.spam_word_count_dict -= intersection
        # self.ham_word_count_dict -= intersection

        # Totally number of spam and ham counts from test suite
        counts = tc.return_spam_ham_count()
        self.spam_count = counts[0]
        self.ham_count = counts[1]

    def test(self, test_corpus_dir):
        '''
        Creates dict of classification and writes it to the file
        :param test_corpus_dir: path to test dir
        :return: None
        '''

        # Prepare "global" variables
        c = Corpus(test_corpus_dir)
        class_dict = {}

        # Iterate over emails with generator in Corpus
        for email in c.emails():
            # Declare probabilities - will be modified
            spam_probability = 0
            ham_probability = 0

            # Get word statistics of email - word frequency and word count
            word_stats = self.get_word_count_for_mail(email[1])
            word_freq = word_stats[0]
            word_count = word_stats[1]

            # Compute spamines of words
            spaminesses = []
            for word in word_freq:
                s = self.get_spaminnes_of_word(word)
                if s is not None:
                    spaminesses.append(s)

            # Caluclates needed parts for further computation
            product = self.prod(spaminesses)
            one_without_spammineses = self.one_without_spaminesses(spaminesses)

            lower = product + one_without_spammineses

            # We cannot divide by zero
            if lower != 0:
                overall_spaminess = product / (product + one_without_spammineses)
            else:
                overall_spaminess = 0

            # Final decision
            if overall_spaminess >= 0.5:
                class_dict.update({email[0]: "SPAM"})
            else:
                class_dict.update({email[0]: "OK"})

        # Creates !prediction.txt file
        utils.write_classification_to_file(test_corpus_dir + "/!prediction.txt", class_dict)

    def get_word_count_for_mail(self, email):
        '''
        Computes word frequency and word count
        :param email: Whole email
        :return: Dictionary of frequencies, word count
        '''

        # Prepare things
        counts = {}
        email.lower()
        email_split = email.split("\n\n")

        # Find words only 3-15 chars -> filters out html stuff and other bullshit
        match_pattern = re.findall(r'\b[a-z]{3,20}\b', email_split[1])

        # Find count for every word
        for word in match_pattern:
            count = counts.get(word, 0)
            counts[word] = count + 1

        frequency_list = counts.keys()

        # Creates dict of frequencies
        dict = {}
        for words in frequency_list:
            dict.update({words: counts[words]})

        # Convert dict to counter, it is easier
        counter = Counter(dict)

        # Count words
        count = len(match_pattern)

        return counter, count

    def get_spaminnes_of_word(self, word):
        '''
        Dumb function - just calculates len of intersection
        :param word: string
        :return: Number of spamminess
        '''

        spam_freq = self.spam_word_count_dict[word]
        ham_freq = self.ham_word_count_dict[word]

        # We cannot divide by zero
        if spam_freq == 0 and ham_freq == 0:
            return None

        return float(spam_freq) / float(spam_freq + ham_freq)

    def prod(self, factors):
        '''
        Returns multiplication of all numbers
        :param factors: numbers to be multiplied
        :return: product
        '''

        return reduce(operator.mul, factors, 1)

    def one_without_spaminesses(self, spaminesses):
        '''
        Counts (1 - s0)(1 - s1) ... (1 - sn)
        :param spaminesses: array of spaminess values
        :return: sum of this row
        '''

        result = 1
        for spaminess in spaminesses:
            result *= (1 - spaminess)

        return result


if __name__ == "__main__":
    f = MyFilter()
    f.train('./data/2')
    f.test('./data/1')

    f.train('./data/1')
    f.test('./data/2')