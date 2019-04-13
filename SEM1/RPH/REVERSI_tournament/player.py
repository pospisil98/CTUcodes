import copy
import game


class MyPlayer:
    ''' Player with move based on weight and position strategy '''

    # ---- INFO ABOUT STRATEGY ----
    # I've given weight to every position on game board, the higher the weight, the better the position.
    # That strategy is good enough to win against random player 7/10 times.
    # Later I decided to improve it with simulating my move and then counting opponents possible moves
    # and choosing the move with best weight and the smallest amount of opponent moves.

    # class constants
    EMPTY = -1
    WEIGHTS = ([100, -10, 10,  3,  3, 10, -10, 100],
              [-10, -20, -3, -3, -3, -3, -20, -10],
              [ 10,  -3,  8,  1,  1,  8,  -3,  10],
              [  3,  -3,  1,  1,  1,  1,  -3,   3],
              [  3,  -3,  1,  1,  1,  1,  -3,   3],
              [ 10,  -3,  8,  1,  1,  8,  -3,  10],
              [-10, -20, -3, -3, -3, -3, -20, -10],
              [100, -10, 10,  3,  3, 10, -10, 100])

    def __init__(self, my_color, opponent_color):
        '''
        Basic constructor
        :param my_color: Color of my stones
        :param opponent_color: Color of opponents stone
        '''

        self.name = 'pospivo1'
        self.my_color = my_color
        self.opponent_color = opponent_color

    def move(self, board):
        '''
        Make a move based on internal rules described above
        :param board: Array with state of playfield
        :return: Best possible move or None if move is not possible
        '''

        valid_moves = self.return_valid_moves(board, self.my_color)
        print(valid_moves)

        if not valid_moves:
            return None
        else:
            return valid_moves[0]['coords']

    def return_possible_moves_from_position(self, position, board, switch=0):
        '''
        Returns possible moves from given coords
        :param position: Tuple of coordinates (row, col)
        :param board: Array with state of playfield
        :param switch: Used to prevent calling return_opponent_moves_count
        :return: Array of possible moves
        '''

        # declare some basic variables
        row_number = position[0]
        col_number = position[1]
        color = board[row_number][col_number]
        opponent_color_len = 0
        valid_moves = []

        # tries to find move in every direction
        for direction in [(1, 0), (-1, 0), (0, 1), (0, -1), (1, 1), (1, -1), (-1, 1), (-1, -1)]:
            # we make new coordinates
            new_col = col_number + direction[0]
            new_row = row_number + direction[1]

            # when coords are okay we might continue and make new value
            if game.is_on_board(new_row, new_col):
                new_val = board[new_row][new_col]

                # go through all stones with opponents color and count it
                while new_val != color and new_val != self.EMPTY:
                    opponent_color_len += 1
                    new_col += direction[0]
                    new_row += direction[1]

                    # if new coords are ok then use them
                    if game.is_on_board(new_row, new_col):
                        new_val = board[new_row][new_col]
                    else:
                        break

                # there we should have coords of first place behind opponent
                if new_val == self.EMPTY and opponent_color_len > 0:
                    weight = self.return_position_weight(new_row, new_col)

                    if switch == 1:
                        # apped moves without opponent count - it would end in endless cycle
                        valid_moves.append({
                            'coords': (new_row, new_col),
                            'len': opponent_color_len,
                            'weight': int(weight)
                        })
                    else:
                        opponent_moves_count = self.return_opponent_moves_count((new_row, new_col), board)
                        valid_moves.append({
                            'coords': (new_row, new_col),
                            'len': opponent_color_len,
                            'opp_count': opponent_moves_count,
                            'weight': int(weight)
                        })

                opponent_color_len = 0
        return valid_moves

    def return_valid_moves(self, board, color, switch=0):
        '''
        Returns all valid moves of given player
        :param board: Array with current state of playfield
        :param color: Color of player which moves we want
        :param switch: Used to prevent calling return_opponent_moves_count
        :return: array of all valid moves sorted by weight
        '''

        valid_moves = []
        my_stones = game.return_all_stones_coords(board, color)

        # for every stone of given color find possible moves from it
        for stone in my_stones:
            poss_moves = self.return_possible_moves_from_position(stone, board, switch)
            for poss in poss_moves:
                # add it only if it doesn't exist
                self.add_move_to_valid_moves(valid_moves, poss_moves)

        # sort by opponent_moves_count and weight and flip it because of the descending sorting
        if switch != 1:
            valid_moves.sort(key=lambda x: (x['opp_count']))
            valid_moves.reverse()
            valid_moves.sort(key=lambda x: (x['weight']))
            valid_moves.reverse()

        return valid_moves

    def return_position_weight(self, row, col):
        '''
        Return weight based on class const array of weights
        :param row: Number of row
        :param col: Number of col
        :return: Integer based weight
        '''

        return self.WEIGHTS[row][col]

    def return_opponent_moves_count(self, move, board):
        '''
        Returns count of opponent moves after we perform our move
        :param move: My move coordinates (row, col)
        :param board: Playfield state
        :return: Number of available moves
        '''

        # copy board because we don't want to change anything in original
        copied_board = copy.deepcopy(board)
        copied_board = game.make_move(copied_board, self.my_color, move)
        valid_moves = self.return_valid_moves(copied_board, self.opponent_color, 1)

        return len(valid_moves)

    def add_move_to_valid_moves(self, valid_moves, possible_moves):
        '''
        Checks existence of move and if its unique then adds it
        :param valid_moves: Array with dicts of valid moves
        :param possible_moves: Array with dicts of possible moves
        '''

        for pos_move in possible_moves:
            counter = 0
            for val_move in valid_moves:
                if val_move['coords'] == pos_move['coords']:
                    counter += 1
            if counter == 0:
                valid_moves.append(pos_move)


if __name__ == "__main__":
    pass
