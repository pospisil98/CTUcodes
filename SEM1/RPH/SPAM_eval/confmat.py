class BinaryConfusionMatrix:
    ''' Class wrapping the BC Matrix '''

    def __init__(self, pos_tag, neg_tag):
        self.pos_tag = pos_tag
        self.neg_tag = neg_tag
        self.tp = 0
        self.tn = 0
        self.fp = 0
        self.fn = 0

    def as_dict(self):
        '''
        Returns matrix as dict of its numbers and description
        :return: dict --> case : count
        '''
        return {"tp" : self.tp, "tn" : self.tn, "fp" : self.fp, "fn" : self.fn}

    def update(self, truth, prediction):
        '''
        Changes values of inner counters
        :param truth: Truth value
        :param prediction: Predicted value
        :return: None
        '''
        if truth not in (self.pos_tag, self.neg_tag) or prediction not in (self.pos_tag, self.neg_tag):
            raise ValueError('Truth or prediction is not valid tag')
        else:
            if truth == self.pos_tag:
                if prediction == self.pos_tag:
                    self.tp += 1
                else:
                    self.fn += 1
            else:
                if prediction == self.pos_tag:
                    self.fp += 1
                else:
                    self.tn += 1

    def compute_from_dicts(self, truth_dict, pred_dict):
        '''
        Computes BCM from two dicts, one with truth and one with predictions
        :param truth_dict: Dict with truths
        :param pred_dict: Dict with predictions
        :return: None
        '''
        for key in truth_dict:
            self.update(truth_dict[key], pred_dict[key])

if __name__ == "__main__":
    cm1 = BinaryConfusionMatrix(pos_tag=True, neg_tag=False)
    print(cm1.as_dict())
    cm1.update(True, True)
    print(cm1.as_dict())

    truth_dict = {'em1': 'SPAM', 'em2': 'SPAM', 'em3': 'OK', 'em4': 'OK'}
    pred_dict = {'em1': 'SPAM', 'em2': 'OK', 'em3': 'OK', 'em4': 'SPAM'}
    cm2 = BinaryConfusionMatrix(pos_tag='SPAM', neg_tag='OK')
    cm2.compute_from_dicts(truth_dict, pred_dict)
    print(cm2.as_dict())
