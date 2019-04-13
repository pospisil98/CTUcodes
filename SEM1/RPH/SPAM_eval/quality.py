''' Module for counting quality from BCM '''
import utils
import confmat

TRUTH_FILE = "!truth.txt"
PREDICTION_FILE = "!prediction.txt"
SPAM_TAG = "SPAM"
HAM_TAG = "OK"


def quality_score(tp, tn, fp, fn):
    '''
    Calculates quality based on formula we've agreed on
    :param tp: TruePositive count
    :param tn: TrueNegative count
    :param fp: FalsePositive count
    :param fn: FalseNegative count
    :return: Number (0 - 1) representing quality
    '''

    return (tp + tn) / (tp + tn + 10 * fp + fn)


def compute_quality_for_corpus(corpus_dir):
    '''
    Calculates quality for corpus
    :param corpus_dir: path to corpus
    :return: Number (0 - 1) representing quality
    '''

    truth = utils.read_classification_from_file(corpus_dir + "/" + TRUTH_FILE)
    prediction = utils.read_classification_from_file(corpus_dir + "/" + PREDICTION_FILE)

    bcm = confmat.BinaryConfusionMatrix(pos_tag=SPAM_TAG, neg_tag=HAM_TAG)
    bcm.compute_from_dicts(truth_dict=truth, pred_dict=prediction)

    return quality_score(bcm.tp, bcm.tn, bcm.fp, bcm.fn)


if __name__ == "__main__":
    pass