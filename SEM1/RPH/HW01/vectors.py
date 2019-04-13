class MyVector:
    ''' Simple class for vectors implementation '''

    def __init__(self, array_of_scalars):
        self.scalars = array_of_scalars

    def get_vector(self):
        return self.scalars

    def __mul__(self, other):
        result = 0
        for scalar_a, scalar_b in zip(self.get_vector(), other.get_vector()):
            result += scalar_a * scalar_b
        return result


if __name__ == "__main__":
    a = MyVector([1,2,3])
    b = MyVector([4,5,6])

    scalar = a * b

    print(scalar)