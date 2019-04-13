import random


class MyPlayer:
    ''' Player copying last action of the opponent with 25% chance on defecting '''

    def __init__(self, payoff_matrix, number_of_iterations = 0):
        ''' Constructs a player '''
        self.payoff_matrix = payoff_matrix
        self.number_of_iterations = number_of_iterations
        self.opponent_moves = []

    def move(self):
        ''' Make a move, False = COOP, True = DEFECT '''

        last = (len(self.opponent_moves) - 1)

        # 25% chance on automatic defecting
        if random.choice([1, 2, 3, 4]) == 1:
            return True
        else:
            # Check for first iteration
            if len(self.opponent_moves) == 0:
                return False
            else:
                # Gets last opponents play and negates it
                last = (len(self.opponent_moves) - 1)
                return not self.opponent_moves[last]

    def record_opponents_move(self, opponent_move):
        ''' Records opponets move and adds it into own memory '''
        self.opponent_moves.append(opponent_move)
        pass
