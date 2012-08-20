package edu.ucsd.cse70.ircclient;

public class Pawn extends ChessPiece
{
  public Pawn(char paramChar)
  {
    super(paramChar);
  }

  public boolean checkValidMove(String paramString1, String paramString2, Board paramBoard)
    throws MovementException
  {
    int i = 0;
    String str = "";
    int j;
    int k;
    int m;
    if (this.color == 'b')
    {
      j = paramString1.charAt(1) - paramString2.charAt(1);
      k = paramString1.charAt(0) - paramString2.charAt(0);
      if (paramString2.charAt(0) == paramString1.charAt(0))
      {
        if (paramString1.charAt(1) == '7')
        {
          if ((j > 0) && (j < 3) && (paramBoard.getPieceAt(paramString2) == 0))
          {
            for (m = paramString1.charAt(1) - '1'; m >= paramString2.charAt(1) - '0'; m--)
            {
              str = paramString1.charAt(0) + "" + m;
              if (paramBoard.getPieceAt(str) != 0)
                continue;
              i++;
            }
            if (i == j)
              return true;
          }
        }
        else if ((paramString1.charAt(1) != '7') && (j > 0) && (j < 2) && (paramBoard.getPieceAt(paramString2) == 0))
          return true;
      }
      else if ((paramString2.charAt(0) != paramString1.charAt(0)) && (j > 0) && (j < 2) && (((k > 0) && (k < 2)) || ((k < 0) && (k > -2) && (paramBoard.getPieceAt(paramString2) > 0) && (paramBoard.getPieceAt(paramString2) < 7))))
        return true;
    }
    if (this.color == 'w')
    {
      j = paramString2.charAt(1) - paramString1.charAt(1);
      k = paramString2.charAt(0) - paramString1.charAt(0);
      if (paramString2.charAt(0) == paramString1.charAt(0))
      {
        if (paramString1.charAt(1) == '2')
        {
          if ((j > 0) && (j < 3) && (paramBoard.getPieceAt(paramString2) == 0))
          {
            for (m = paramString1.charAt(1) - '/'; m <= paramString2.charAt(1) - '0'; m++)
            {
              str = paramString1.charAt(0) + "" + m;
              if (paramBoard.getPieceAt(str) != 0)
                continue;
              i++;
            }
            if (i == j)
              return true;
          }
        }
        else if ((paramString1.charAt(1) != '2') && (j > 0) && (j < 2) && (paramBoard.getPieceAt(paramString2) == 0))
          return true;
      }
      else if ((paramString2.charAt(0) != paramString1.charAt(0)) && (j > 0) && (j < 2) && (((k > 0) && (k < 2)) || ((k < 0) && (k > -2) && (paramBoard.getPieceAt(paramString2) > -7) && (paramBoard.getPieceAt(paramString2) < 0))))
        return true;
    }
    throw new MovementException("Pawn");
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.Pawn
 * JD-Core Version:    0.6.0
 */