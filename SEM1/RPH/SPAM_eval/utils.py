''' Module with some useful functions for spam filter '''


def read_classification_from_file(path_to_file):
    '''
    Reads clasification from file and returns dict of it
    :param path_to_file: path to file with some clasification
    :return: Dict --> filename : classification tag
    '''
    dct = {}

    with open(path_to_file, mode='r', encoding="utf-8") as file:
        for line in file:
            split = line.split()
            dct[split[0]] = split[1]
    return dct


def write_classification_to_file(path_to_file, dct):
    '''
    Writes dict with classification to specified file
    :param path_to_file: Path where it should be saved
    :param dct: dict with classification
    :return: None
    '''
    with open(path_to_file, mode='w', encoding="utf-8") as file:
        for key, value in dct.items():
            file.write("{0} {1}\n".format(key, value))


if __name__ == "__main__":
    dictionary = read_classification_from_file("./data/1/!truth.txt")
    write_classification_to_file("./data/test.txt", dictionary)