Welcome to text based Chess! This is a two player only version of Chess in which you control your pieces using algebraic notation.
The game will create a history of your moves as you play the game, and can save and load your game. 
When you're done playing, you can look at the log and try to improve on your gameplay! Enjoy!

How to Use Algebraic Notation

8 |

7 |

6 |

5 |

4 |

3 |

2 |

1 |

      _    _    _    _    _    _    _    _
    
      a    b    c    d    e    f    g    h

The chessboard has axes like this when you play. Each square is assigned a coordinate according to these axes.
To move your piece, you follow this format:

[first letter of piece's name][desired destination coordinates]

When using this format, White's pieces are capitalized, while Black's are lowercased. 
Coordinates should be written with the letter first.
When moving the knight, use "N/n" to control it since King already has "K/k".
An example of a valid move would be: Pa3, which moves White's leftmost pawn one square up.

Special Commands
- "0-0" for a king's side castle, "0-0-0" for a queen's side castle
- If two pieces can move to the same square, you can use 

  [first letter of piece's name][piece's coordinates][desired destination] 
  to disambiguate them
- "save" to save the game and return to it later (only one game can be saved at a time)
- "load" to load whatever game is currently saved
