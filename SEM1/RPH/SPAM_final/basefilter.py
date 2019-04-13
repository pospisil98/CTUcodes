class BaseFilter:
    ''' Class mainly used just for sharing those four constants '''

    SPAM_TAG = "SPAM"
    HAM_TAG = "OK"
    TRUTH_FILE = "!truth.txt"
    PREDICTION_FILE = "./!prediction.txt"

    def __init__(self):
        pass

    def train(self, path):
        pass

    def test(self, path):
        pass