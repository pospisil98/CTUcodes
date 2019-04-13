'''
Library for game related functions for the Reversi game.

SOME FUNCTIONS ARE FROM PROVIDED game.py
'''


# some constants
EMPTY = -1
BOARD_SIZE = 8


def make_move(board, color, move):
    '''
    Makes given move on board and rotates according stones.
    We expect the move to be valid!
    :param board: Copy of playfield
    :param color: Color of player stone
    :param move: Coords of move
    :return: New playfield state
    '''

    board[move[0]][move[1]] = color
    dx = [-1, -1, -1, 0, 1, 1, 1, 0]
    dy = [-1, 0, 1, 1, 1, 0, -1, -1]
    for i in range(len(dx)):
        if confirm_direction(board, move, dx[i], dy[i], color):
            change_stones_in_direction(board, move, dx[i], dy[i], color)
    return board


def confirm_direction(board, move, dx, dy, players_color):
    '''
    Looks into dirextion [dx,dy] to find if the move in this dirrection is correct.
    It means that first stone in the direction is oponents and last stone is players.
    :param board: array with game state
    :param move: position where the move is made [x,y]
    :param dx: x direction of the search
    :param dy: y direction of the search
    :param players_color: color of the movemaker
    :return: True if move in this direction is correct
    '''
    if players_color == 1:
        opponents_color = 0
    else:
        opponents_color = 1

    posx = move[0] + dx
    posy = move[1] + dy
    if (posx >= 0) and (posx < BOARD_SIZE) and (posy >= 0) and (posy < BOARD_SIZE):
        if board[posx][posy] == opponents_color:
            while (posx >= 0) and (posx < BOARD_SIZE) and (posy >= 0) and (posy < BOARD_SIZE):
                posx += dx
                posy += dy
                if (posx >= 0) and (posx < BOARD_SIZE) and (posy >= 0) and (posy < BOARD_SIZE):
                    if board[posx][posy] == EMPTY:
                        return False
                    if board[posx][posy] == players_color:
                        return True

    return False


def change_stones_in_direction(board, move, dx, dy, players_color):
    posx = move[0] + dx
    posy = move[1] + dy
    while (not (board[posx][posy] == players_color)):
        board[posx][posy] = players_color
        posx += dx
        posy += dy


def is_on_board(row, col, row_count=8, col_count=8):
    '''
    Check coords existence on playfield
    :param row: Number of position row
    :param col: Number of position col
    :param row_count: Number of playfield rows
    :param col_count: Number of playfield cols
    :return:
    '''

    if row < 0 or col < 0:
        return False
    elif row >= row_count or col >= col_count:
        return False
    else:
        return True


def return_stone_color_count(board, color):
    '''
    Returns number of stones of given color
    :param board: Array with state of playfield
    :param color: Color of searched stones
    :return:
    '''

    stone_count = 0

    for row_number, row in enumerate(board):
        for col_number, col in enumerate(row):
            # if we find desired color - count it
            if col == color:
                stone_count += 1

    return stone_count


def return_all_stones_coords(board, color):
    '''
    Find coordinates of stones of given color
    :param board: Array with state of playfield
    :param color: Color of player
    :return Array of tuples of coordinates
    '''

    coords = []

    for row_number, row in enumerate(board):
        for col_number, col in enumerate(row):
            # if we find our color - append its coords
            if col == color:
                coords.append((row_number, col_number))

    return coords
