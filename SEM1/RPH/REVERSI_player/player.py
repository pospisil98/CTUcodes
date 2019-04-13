import random

class MyPlayer:
    ''' Player that just plays random valid move '''
    EMPTY = -1

    ''' Simple reversi player - sumb '''
    def __init__(self, my_color, opponent_color):
        self.name = 'pospivo1'
        self.my_color = my_color
        self.opponent_color = opponent_color

    def move(self, board):
        dict = self.return_valid_moves(board)
        moves = []

        for val in dict:
            moves.append(val['coords'])

        return random.choice(moves)

    def return_valid_moves(self, board):
        # iterates through rows and columns of board
        number_of_rows = len(board)
        number_of_cols = len(board[0])
        opponent_color_len = 0
        valid_moves = []

        for row_number, row in enumerate(board):
            for col_number, col in enumerate(row):
                # if we find our color - start to search for moves in every direction
                if col == self.my_color:
                    for direction in [(1, 0), (-1, 0), (0, 1), (0, -1), (1, 1), (1, -1), (-1, 1), (-1, -1)]:
                        # we make new coordinates
                        new_col = col_number + direction[0]
                        new_row = row_number + direction[1]

                        # when coords are okay we might continue and make new value
                        if self.is_on_board(new_row, new_col, number_of_rows, number_of_cols):
                            new_val = board[new_row][new_col]

                            # go through all stones with opponents color
                            while new_val == self.opponent_color:
                                opponent_color_len += 1
                                new_col += direction[0]
                                new_row += direction[1]

                                if self.is_on_board(new_row, new_col, number_of_rows, number_of_cols):
                                    new_val = board[new_row][new_col]
                                else:
                                    break

                            # there we should have coords of first place behind opponent
                            if new_val == self.EMPTY and opponent_color_len > 0:
                                valid_moves.append({
                                    'coords' : (new_row, new_col),
                                    'len' : opponent_color_len
                                })

                            opponent_color_len = 0;
        return valid_moves

    def is_on_board(self, y, x, row_count, col_count):
        ''' Checks if given coords are in range of array '''
        if y < 0 or x < 0:
            return False
        elif y >= row_count or x >= col_count:
            return False
        else:
            return True

if __name__ == "__main__":
    pass
