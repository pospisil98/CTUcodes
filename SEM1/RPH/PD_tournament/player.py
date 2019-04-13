import copy
import random

COOP = False
DEFECT = True


class MyPlayer:
    ''' Player that computes move value or plays DevilTitForTat when possible'''

    # Info about my strategy
    # Because of the irregularity of the payoff_matrix I've decided not to use any of the clasical strategies.
    # Instead I've tried to calculate value of every combination of moves and find the most valuable one.
    # I know that it isn't probably the best method but I wasn't able to create something better.
    #
    # I always try to be polite and cooperate because it could maximalize our score.
    #
    # As a bonus this player has implemented switch for turning on/off the "DevilTitForTat" strategy
    # in case of payoff_matrix being good for it and because we can switch the "inteligence" in one round.

    def __init__(self, payoff_matrix, number_of_iterations=0):
        ''' Constructs a player '''

        self.payoff_matrix = payoff_matrix
        self.number_of_iterations = number_of_iterations
        self.opponent_moves = []
        self.my_moves = []
        self.convenience = self.analyze_payoff_matrix_into_convenience()
        self.game_type_count = [1, 1, 1, 1]
        self.switch = 0

        if self.is_game_valid_for_dtft():
            self.switch = 1

    def move(self):
        ''' Make a move, False = COOP, True = DEFECT '''

        if self.switch == 0:
            # decrypt the index based on order of game_type_count
            index = self.get_most_valuable_move()
            if index == 0 or index == 2:
                move = COOP
            else:
                move = DEFECT
            self.my_moves.append(move)
        else:
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

        print(move)
        return move

    def record_opponents_move(self, opponent_move):
        ''' Records opponets move and adds it into own memory '''

        self.opponent_moves.append(opponent_move)

        # recalculates the game_type_count array (it starts with 1 because of the usage in division)
        if self.switch == 0:
            self.update_game_type_count()

        pass

    def analyze_payoff_matrix_into_convenience(self):
        ''' Builds a basic set of data for decisions later on game '''

        # profit means how many points i get from every possible combination of moves
        my_profit = self.get_my_profit()
        opponents_profit = copy.deepcopy(my_profit)
        opponents_profit.reverse()

        # ratio is the fraction of the my and opponents profit
        my_ratio = []
        for my, opp in (zip(my_profit, opponents_profit)):
            my_ratio.append(my/opp)

        opponents_ratio = copy.deepcopy(my_ratio)
        opponents_ratio.reverse()

        # convenience is the thing that we want, the combination of profit and ratio
        my_convenience = []
        for profit, ratio in (zip(my_profit, my_ratio)):
            my_convenience.append(profit*ratio)

        opponents_convenience = copy.deepcopy(my_convenience)
        opponents_convenience.reverse()

        return my_convenience, opponents_convenience

    def is_game_valid_for_dtft(self):
        ''' Based on matrix decides if its better to play DTFT or own calculating strategy '''

        coopAvg = (self.payoff_matrix[COOP][COOP][0] + self.payoff_matrix[COOP][COOP][1]) / 2
        defAvg = (self.payoff_matrix[DEFECT][DEFECT][0] + self.payoff_matrix[DEFECT][DEFECT][1]) / 2
        mixAvg = (self.payoff_matrix[COOP][DEFECT][0] + self.payoff_matrix[COOP][COOP][0]) / 2

        if coopAvg >= defAvg and coopAvg <= mixAvg:
            return True
        else:
            return False

    def get_my_profit(self):
        ''' Returns array of poins from every move (iterates columns) '''

        profit = []
        profit.append(self.payoff_matrix[COOP][COOP][0])
        profit.append(self.payoff_matrix[DEFECT][COOP][0])
        profit.append(self.payoff_matrix[COOP][DEFECT][0])
        profit.append(self.payoff_matrix[DEFECT][DEFECT][0])

        # check if some profit is 0 - because of the afterward division
        # if it is, then add 1 to every element
        if 0 in profit:
            for x in range(len(profit)):
                profit[x] += 1

        return profit

    def update_game_type_count(self):
        ''' Determines last game type based on last moves '''

        if len(self.my_moves) == 0:
            my_last = self.my_moves[0]
        else:
            my_last = self.my_moves[len(self.my_moves) - 1]

        if len(self.opponent_moves) == 0:
            opp_last = self.opponent_moves[0]
        else:
            opp_last = self.opponent_moves[len(self.opponent_moves) - 1]

        if my_last == COOP and opp_last == COOP:
            self.game_type_count[0] += 1
        elif my_last == DEFECT and opp_last == COOP:
            self.game_type_count[1] += 1
        elif my_last == COOP and opp_last == DEFECT:
            self.game_type_count[2] += 1
        elif my_last == DEFECT and opp_last == DEFECT:
            self.game_type_count[3] += 1

    def get_most_valuable_move(self):
        ''' Returns index of most valuable move based on order in game_type_count '''

        my_valuability = []
        iterator = 0
        for my in self.convenience[0]:
            my_valuability.append(my * self.game_type_count[iterator])
            iterator += 1

        # finds max and index of max
        max_value = max(my_valuability)
        max_index = my_valuability.index(max_value)

        return max_index


if __name__ == "__main__":
    payoff_matrix = [[(3, 3), (2, 4)], [(4, 2), (1, 1)]]
    p1 = MyPlayer(payoff_matrix)
