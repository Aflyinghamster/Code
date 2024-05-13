import pygame 
pygame.init() 
#draw initial screen 
WIDTH = 1000 
HEIGHT = 900 
screen = pygame.display.set_mode([WIDTH, HEIGHT]) 
pygame.display.set_caption('Chess Game')
#create a bunch of font sizes 
font = pygame.font.Font('freesansbold.ttf', 20)
med_font = pygame.font.Font('freesansbold.ttf', 35) 
lrg_font = pygame.font.Font('freesansbold.ttf', 50) 
#create timer
timer = pygame.time.Clock()
fps = 60 

#game variables and images
#list of strings for each of the pieces for the respective colours 
white_pieces = ['rook', 'knight','bishop', 'king','queen','bishop', 'knight', 'rook',
                'pawn','pawn','pawn','pawn','pawn','pawn','pawn','pawn',] 

black_pieces = ['rook', 'knight','bishop', 'king','queen','bishop', 'knight', 'rook',
                'pawn','pawn','pawn','pawn','pawn','pawn','pawn','pawn',] 
#array of the locations default top left is (0,0) bottom right (7,7)
white_locations = [(0,0), (1,0), (2,0), (3,0), (4,0), (5,0), (6,0), (7,0),
                     (0,1), (1,1), (2,1), (3,1),(4,1) ,(5,1) ,(6,1) ,(7,1)] 

black_locations = [(0,7), (1,7), (2,7), (3,7), (4,7), (5,7), (6,7), (7,7),
                     (0,6), (1,6), (2,6),  (3,6), (4,6), (5,6), (6,6), (7,6)]

#list of the captured pieces 
captured_pieces_white = []
captured_pieces_black = [] 

# 0 whites turn no selection, 1 whites turn pieces selected, 2 black no selection 3 black selection
turn_phase = 0 
selection = 100 

#stores a list of valid moves 
valid_moves = [] 

#load in game images assets 
black_queen = pygame.image.load('Assets/black queen.png')
black_queen = pygame.transform.scale(black_queen,(80,80))
#smaller version to be used on captured display 
black_queen_small = pygame.transform.scale(black_queen,(40,40)) 

black_king = pygame.image.load('Assets/black king.png')
black_king = pygame.transform.scale(black_king, (80, 80))
black_king_small = pygame.transform.scale(black_king, (45, 45))

black_rook = pygame.image.load('Assets/black rook.png')
black_rook = pygame.transform.scale(black_rook, (80, 80))
black_rook_small = pygame.transform.scale(black_rook, (45, 45))

black_bishop = pygame.image.load('Assets/black bishop.png')
black_bishop = pygame.transform.scale(black_bishop, (80, 80))
black_bishop_small = pygame.transform.scale(black_bishop, (45, 45))

black_knight = pygame.image.load('Assets/black knight.png')
black_knight = pygame.transform.scale(black_knight, (80, 80))
black_knight_small = pygame.transform.scale(black_knight, (45, 45))

black_pawn = pygame.image.load('Assets/black pawn.png')
black_pawn = pygame.transform.scale(black_pawn, (65, 65))
black_pawn_small = pygame.transform.scale(black_pawn, (45, 45))

white_queen = pygame.image.load('Assets/white queen.png')
white_queen = pygame.transform.scale(white_queen, (80, 80))
white_queen_small = pygame.transform.scale(white_queen, (45, 45))

white_king = pygame.image.load('Assets/white king.png')
white_king = pygame.transform.scale(white_king, (80, 80))
white_king_small = pygame.transform.scale(white_king, (45, 45))

white_rook = pygame.image.load('Assets/white rook.png')
white_rook = pygame.transform.scale(white_rook, (80, 80))
white_rook_small = pygame.transform.scale(white_rook, (45, 45))

white_bishop = pygame.image.load('Assets/white bishop.png')
white_bishop = pygame.transform.scale(white_bishop, (80, 80))
white_bishop_small = pygame.transform.scale(white_bishop, (45, 45))

white_knight = pygame.image.load('Assets/white knight.png')
white_knight = pygame.transform.scale(white_knight, (80, 80))
white_knight_small = pygame.transform.scale(white_knight, (45, 45))

white_pawn = pygame.image.load('Assets/white pawn.png')
white_pawn = pygame.transform.scale(white_pawn, (65, 65))
white_pawn_small = pygame.transform.scale(white_pawn, (45, 45)) 

#create a list of the images 
white_images = [white_pawn,white_queen,white_king, white_knight, white_rook, white_bishop]

black_images = [black_pawn, black_queen, black_king, black_knight, black_rook, black_bishop]  

# create a list of the small images 
small_white_images = [white_pawn_small ,white_queen_small, white_king_small, white_knight_small, white_rook_small, white_bishop_small]

small_black_images = [black_pawn_small, black_queen_small, black_king_small, black_knight_small, black_rook_small, black_bishop_small]  

# create a list of strings where the index of the piece is the same as in the list of images 
piece_list = ['pawn','queen','king','knight','rook','bishop']

#check variables and flashing counter 
counter = 0 
winner = '' 
game_over = False
# draw main game board there are 64 total rectangles but we only draw half as we will use the back ground in the other half, so this function draws squares with spaces 32x 
def draw_board():
    for i in range(32):
        column = i % 4 # this is the remainder function 
        row = i // 4 # this is floor division will always round down so when i = 1 the row will = 0 until the 8th square which will cause the row to increase by 1 
        if row % 2 == 0: 
            pygame.draw.rect(screen, 'light gray', [600 -(column*200), row * 100, 100, 100 ] ) #done to leave space on side will move ro1 by 100px each time and draw a square of 100x100px
        else: 
            pygame.draw.rect(screen, 'light gray', [700 -(column*200), row * 100, 100, 100 ] ) #offsets the drawn squares horizontally
       #extra rectangles on edges of screen for other ui elements 
        pygame.draw.rect(screen, 'gray',[0,800, WIDTH, 100]) 
        pygame.draw.rect(screen, 'gold',[0,800, WIDTH, 100], 5)
        pygame.draw.rect(screen, 'gold',[800,0, 200, HEIGHT], 5)
        status_text= ['white select a piece','white select destination',
                      'black select piece','black select destination']
        screen.blit(lrg_font.render(status_text[turn_phase], True,'black'),(20,820)) 
        # draw additional lines to add def to the board by creating a for loop to iterate 9x 
        for i in range(9):
            pygame.draw.line(screen,'black',(0,100*i), (800,100*i),2 )  #(screen, color, (x),(y), width) horizontal 
            pygame.draw.line(screen,'black',(100*i,0), (100*i,800),2 )
        screen.blit(med_font.render('FORFEIT', True, 'black'), (810, 810))

#draw the pieces onto the board by running a for loop through the lists of image and string pairs 
def draw_pieces(): #draws white pieces first 
    for i in range(len(white_pieces)): # finds the index value of the piece we are looking in the list and uses it to provide the correct image 
        index = piece_list.index(white_pieces[i]) 
        if white_pieces[i] == 'pawn':
            screen.blit(white_pawn, (white_locations[i][0] *100 +22, white_locations[i][1]*100 +30)) # 0 gets the x cord and 1 gives the y cord for all pieces still in the list check the value from the locations and loads the appropriate asses 
        else: 
            screen.blit(white_images[index], (white_locations[i][0] *100 +10, white_locations[i][1]*100 +10))
        if turn_phase < 2: #draws a red rectangle round the currently selected piece on whites turn
             if selection == i:
                 pygame.draw.rect(screen, 'red', [white_locations[i][0]*100 +1, white_locations[i][1] *100+1, 100,100],1) 

    #draw black pieces 
    for i in range(len(black_pieces)): 
        index = piece_list.index(black_pieces[i]) 
        if black_pieces[i] == 'pawn':
            screen.blit(black_pawn, (black_locations[i][0] *100 +22, black_locations[i][1]*100 +30))  
        else: 
            screen.blit(black_images[index], (black_locations[i][0] *100 +10, black_locations[i][1]*100 +10))
        if turn_phase >= 2: #draws a blue rectangle round the currently selected piece on blacks turn
             if selection == i:
                 pygame.draw.rect(screen, 'blue', [black_locations[i][0]*100 +1, black_locations[i][1]*100+1, 100,100],1)

# function to check all pieces valid options on the board
def check_options(pieces, locations, turn): #pass in the list of pieces location and what turn it is 
    moves_list = []
    all_moves_list = [] 
    for i in range((len(pieces))): #create a loop that checks all available moves
        location = locations[i] #  looking at locations list at i for all pieces ion the piece list
        piece = pieces[i]
        if piece == 'pawn': #call different checking function for each piece 
         moves_list = check_pawn(location, turn)
        elif piece == 'rook':
            moves_list = check_rook(location, turn)
        elif piece == 'knight':
            moves_list = check_knight(location, turn)
        elif piece == 'bishop':
            moves_list = check_bishop(location, turn) 
        elif piece == 'queen':
            moves_list = check_queen(location, turn)
        elif piece == 'king':
            moves_list = check_king(location, turn)
        
         
        all_moves_list.append(moves_list)
    
    return all_moves_list

# check valid pawn moves by adding 1 to the y position for whit end -1 for black includes case for moving 2 at start 
def check_pawn(position, colour):
    moves_list = []
    if colour == 'white': 
        ### gets the x and y coord checks one piece below the current piece if the location is not in the white or black locations and is on the board list it is a valid move
        if (position[0], position[1]+1) not in white_locations and \
              (position[0], position[1]+1) not in black_locations and position[1] < 7:
            moves_list.append((position[0], position[1]+1)) 

            #pawn can only move 2 squares if its position is on the starting square
        if (position[0], position[1]+2) not in white_locations and \
              (position[0], position[1]+2) not in black_locations and position[1] == 1:
            moves_list.append((position[0], position[1]+2)) 

        #check right diagonal only needs to check if a black pices is there
        if (position[0]+1, position[1]+1) in black_locations:
            moves_list.append((position[0]+1, position[1]+1)) 
       
        #check left 
        if (position[0]-1, position[1]+1) in black_locations:
            moves_list.append((position[0]-1, position[1]+1))   

    else:
        ### gets the x and y coord checks one piece below the current piece if the location is not in the white or black locations and is on the board list it is a valid move
        if (position[0], position[1]-1) not in white_locations and \
              (position[0], position[1]-1) not in black_locations and position[1] > 0 :
            
            moves_list.append((position[0], position[1]-1)) 
            
            #pawn can only move 2 squares if its position is on the starting square
        if (position[0], position[1]-2) not in white_locations and \
              (position[0], position[1]-2) not in black_locations and position[1] == 6:
            
            moves_list.append((position[0], position[1]-2)) 
        #check right diagonal only needs to check if a black pices is there
        
        if (position[0]+1, position[1]-1) in white_locations:
            
            moves_list.append((position[0]+1, position[1]-1))  
        
        #check left 
        if (position[0]-1, position[1]-1) in white_locations:
            moves_list.append((position[0]-1, position[1]-1))   
    return moves_list
#check valid rook moves using a while loop to create a chain to list all possible moves 
def check_rook(position, colour):
    moves_list = [] 
    if colour == 'white': # how to tell apart friendly pices based on which colour is playing 
        enemies_list = black_locations
        friends_list = white_locations
    else:
         friends_list = black_locations 
         enemies_list = white_locations 
    for i in range(4): #are written in the following order down, up, right, left  when going up or down x position doesn't change but y does and vice versa
        path = True 
        chain = 1 
        if i == 0: 
            x = 0 
            y = 1
        elif i == 1:
             x = 0
             y = -1
        elif i == 2:
            x = 1 
            y = 0
        else: 
            x = -1
            y = 0 
        while path: # while path is true checks to see if can continue going ina direction  has to be either empty or enemy 
            if (position[0]+ (chain * x), position[1] +(chain *y)) not in friends_list and \
              0 <= position[0] + (chain * x) <= 7 and 0 <= position[1] + (chain * y) <=7:
                
                moves_list.append((position[0] + (chain * x), position[1] + (chain * y)))
                # if is enemy cant go further in that direction
                if (position[0] + (chain * x ), position[1]+(chain*y)) in enemies_list:
                    path = False 
                chain += 1
            else:
                path = False
    return moves_list
#check valid knight done by checking all the possible moves that could be made and seeing if empty or enemy
def check_knight(position, colour):
    moves_list = []
    if colour == 'white': # how to tell apart friendly pices based on which colour is playing 
        enemies_list = black_locations
        friends_list = white_locations
    else:
         friends_list = black_locations 
         enemies_list = white_locations 
# 8 squares to check for knights 2 squares 1 dir 1 in the other  targets is a list containing the moves then check is each of these is ona white square on an enemy square
    targets = [(1,2), (1,-2), (2,1), (2,-1),(-1,2), (-1,-2), (-2,1), (-2,-1)] #possible moves
    for i in range(8):
       target = (position[0] + targets[i][0], position[1] + targets[i][1])  #set target to for each possible move from targets
       if target not in friends_list and 0 <= target[0] <=7 and 0 <= target[1] <=7: #if the target isn't in the friends list and is within the range of the board
           moves_list.append(target) #add the move to the move list 
    return moves_list
# check valid bishop moves same as rook but moves in diagonal lines        
def check_bishop(position, colour):
    moves_list = []
    if colour == 'white': # how to tell apart friendly pices based on which colour is playing 
        enemies_list = black_locations
        friends_list = white_locations
    else:
         friends_list = black_locations 
         enemies_list = white_locations 
    for i in range(4): #are written in the following order  upright, up left down right , down left when going up or down ,x position doesn't change but y does and vice versa
        path = True 
        chain = 1 
        if i == 0: 
            x = 1 
            y = -1
        elif i == 1:
             x = -1
             y = -1
        elif i == 2:
            x = 1 
            y = 1
        else: 
            x = -1
            y = 1
        while path:
            if (position[0]+ (chain * x), position[1] +(chain *y)) not in friends_list and \
                    0 <= position[0] + (chain * x) <= 7 and 0 <= position[1] + (chain * y) <=7:
                moves_list.append((position[0] + (chain * x), position[1] + (chain * y)))
                # if is enemy cant go further in that direction
                if (position[0] + (chain * x ), position[1]+(chain*y)) in enemies_list:
                    path = False 
                chain += 1
            else:
                path = False

    return moves_list
#check valid moves for queen, this is a combination of both the bishop and the rook as we have already defined this we can create the move list using prev func
def check_queen(position, colour):
    moves_list = check_bishop(position, colour)
    second_list = check_rook(position,colour)
    for i in range(len(second_list)):
        moves_list.append(second_list[i])
    return moves_list
#king moves, same number of moves as the knight so reused code
def check_king(position, colour):  
    moves_list = []
    if colour == 'white': # how to tell apart friendly pices based on which colour is playing 
        enemies_list = black_locations
        friends_list = white_locations
    else:
         friends_list = black_locations 
         enemies_list = white_locations 
    
    targets = [(1,0), (1,1), (1,-1), (-1,0),(-1,1), (-1,-1), (0,1), (0,-1)] #possible moves
    for i in range(8):
       target = (position[0] + targets[i][0], position[1] + targets[i][1])  #set target to for each possible move from targets
       if target not in friends_list and 0 <= target[0] <=7 and 0 <= target[1] <=7: #if the target isn't in the friends list and is within the range of the board
           moves_list.append(target) #add the move to the move list 
    return moves_list

# check valid moves for only the selected piece 
def check_valid_moves():

    if turn_phase < 2:
        options_list = white_options
    else:
        options_list = black_options
    valid_options = options_list[selection]
    return valid_options



 #draw the valid moves on screen 

def draw_valid(moves):
        if turn_phase < 2:
            colour = 'red'
        else:
            colour = 'blue'
        for i in range(len(moves)):
            pygame.draw.circle(screen, colour, (moves[i][0] *100+50 , moves[i][1] *100+ 50 ), 5)
# draw captured pieces on side of screen 
def draw_captured():
    for i in range(len(captured_pieces_white)):
        captured_piece = captured_pieces_white[i] # current cap piece = the index of the captures piece
        index = piece_list.index(captured_piece) # find what index it is in the piece list to load the correct image 
        screen.blit(small_black_images[index],(825,5+50 * i)) 
    for i in range(len(captured_pieces_black)):
        captured_piece = captured_pieces_black[i]
        index = piece_list.index(captured_piece)
        screen.blit(small_white_images[index],(925,5 + 50 * i))
## function for drawing flashing square when in check     
def draw_check():
    if turn_phase < 2: #whites turn
        if 'king' in white_pieces:
            king_index = white_pieces.index('king') #is the white king in check
            king_location = white_locations[king_index]
            for i in range(len(black_options)): # check if king position is in any black options 
                 if king_location in black_options[i]:
                    if counter < 15: # draw a rect fof half the timer
                          pygame.draw.rect(screen, 'dark red', [white_locations[king_index][0]* 100 + 1,
                                                            white_locations[king_index][1] * 100 +1,100,100],15) 
        
    else: 
        if 'king' in black_pieces:
             king_index = black_pieces.index('king') #is the white king in check
             king_location = black_locations[king_index]
             for i in range(len(white_options)): # check if king position is in any black options 
                 if king_location in white_options[i]:
                     if counter < 15: # draw a rect fof half the timer
                         pygame.draw.rect(screen, 'dark blue', [black_locations[king_index][0]* 100 + 1,
                                                            black_locations[king_index][1] * 100 +1,100,100],15)

def draw_game_over():
    pygame.draw.rect(screen, 'black', [200,200,400,70])
    screen.blit(font.render(f'{winner} Won the game', True, 'white'), (210, 210))      
    screen.blit(font.render(f'PRESS ENTER TO RESTART', True, 'white'), (240, 240))             


black_options = check_options(black_pieces, black_locations, 'black')
white_options = check_options(white_pieces, white_locations, 'white') #load the initial fist options
run = True
while run:
    timer.tick(fps)
    if counter < 30:
        counter +=1 
    else:
        counter = 0
    screen.fill('dark gray')
    draw_board()
    draw_pieces()
    draw_captured()
    draw_check() 
    
    if selection != 100:
        valid_moves = check_valid_moves()
        draw_valid(valid_moves)

    #event handling 
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            run = False
        if event.type == pygame.MOUSEBUTTONDOWN and event.button == 1 and not game_over: ##if the left mouse button is pressed
            x_coord = event.pos[0] // 100  # gets x pixel position divides by 100 to get x coord 
            y_coord = event.pos[1] // 100 # y
            click_coords = (x_coord, y_coord) #creates a tuple to find the x and y position when the mouse is pressed
            if turn_phase <= 1: #if it is whites turn & the coords are in a place where there is a white piece from the white locations list change to the next turn phase
                if click_coords == (8, 8) or click_coords == (9,8):
                    winner = 'black'
                if click_coords in white_locations:
                    selection = white_locations.index(click_coords)
                    if turn_phase == 0:
                        turn_phase = 1
                if click_coords in valid_moves and selection != 100: #if selection is not the default dummy variable and the click coords is found in the valid moves list
                    white_locations[selection] = click_coords #update the index for the selected pice with the new x and y values 
                    if click_coords in black_locations: #if the new click coords have the same index as a pice in black locations # add that pice to the captured pieces list 
                        black_piece = black_locations.index(click_coords) 
                        captured_pieces_white.append(black_pieces[black_piece])
                        if black_pieces[black_piece] == 'king':
                            winner = 'white'
                        black_pieces.pop(black_piece) # remove the capture pice from both the pieces and locations lists 
                        black_locations.pop(black_piece) 
                    black_options = check_options(black_pieces, black_locations, 'black')
                    white_options = check_options(white_pieces, white_locations, 'white')
                    
                    turn_phase = 2 #set turn phase to 2 for blacks turn 
                    selection = 100 #reset selection
                    valid_moves = [] #clear the valid_moves list 

            if turn_phase > 1: #as before but for blacks turn 
                if click_coords == (8, 8) or click_coords == (9,8):
                    winner = 'white'
                if click_coords in black_locations:
                    selection = black_locations.index(click_coords)
                    print(selection)
                    if turn_phase == 2:
                        turn_phase= 3
                if click_coords in valid_moves and selection != 100:
                    black_locations[selection] = click_coords
                    if click_coords in white_locations:
                        white_piece = white_locations.index(click_coords) 
                        captured_pieces_black.append(white_pieces[white_piece])
                        if white_pieces[white_piece] == 'king':
                           winner = 'black'
                        white_pieces.pop(white_piece)
                        white_locations.pop(white_piece)

                    black_options = check_options(black_pieces, black_locations, 'black')
                    white_options = check_options(white_pieces, white_locations, 'white')

                    turn_phase = 0
                    selection = 100 
                    valid_moves = []
        if event.type == pygame.KEYDOWN and game_over: # if game over and enter pressed restart game re initialize
            if event.key == pygame.K_RETURN:
                game_over = False 
                winner ='' 
                white_pieces = ['rook', 'knight','bishop', 'king','queen','bishop', 'knight', 'rook',
                'pawn','pawn','pawn','pawn','pawn','pawn','pawn','pawn',] 

                black_pieces = ['rook', 'knight','bishop', 'king','queen','bishop', 'knight', 'rook',
                'pawn','pawn','pawn','pawn','pawn','pawn','pawn','pawn',] 
                  #array of the locations default top left is (0,0) bottom right (7,7)
                white_locations = [(0,0), (1,0), (2,0), (3,0), (4,0), (5,0), (6,0), (7,0),
                     (0,1), (1,1), (2,1), (3,1),(4,1) ,(5,1) ,(6,1) ,(7,1)] 

                black_locations = [(0,7), (1,7), (2,7), (3,7), (4,7), (5,7), (6,7), (7,7),
                     (0,6), (1,6), (2,6),  (3,6), (4,6), (5,6), (6,6), (7,6)] 
                captured_pieces_white = []
                captured_pieces_black = [] 
                turn_phase = 0
                selection = 100
                valid_moves =[] 
                black_options = check_options(black_pieces, black_locations, 'black')
                white_options = check_options(white_pieces, white_locations, 'white') 


    if winner != '':
        game_over = True
        draw_game_over()


    pygame.display.flip()
pygame.quit()