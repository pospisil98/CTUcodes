import os


class Corpus:
    ''' Class wrapping the file folder '''

    TRUTHFILE = "!truth.txt"
    PREDFILE = "!prediction.txt"

    def __init__(self, path):
        self.path = path

    def emails(self):
        '''
        Generator for going through the email folder
        :return: Tuple with filename and mail content
        '''

        for filename in os.listdir(self.path):
            if filename[0] != '!':
                with open(self.path + "/" + filename, mode='r', encoding="utf-8") as file:
                    mail = file.read()
                    yield (filename, mail)


if __name__ == "__main__":
    corpus = Corpus("./data/1")

    count = 0
    # Go through all emails and print the filename and the message body
    for fname, body in corpus.emails():
        print(fname)
        print(body)
        print('-------------------------')
        count += 1
    print('Finished: ', count, 'files processed.')
