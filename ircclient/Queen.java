package edu.ucsd.cse70.ircclient;

public class Queen extends ChessPiece
{
  public Queen(char paramChar)
  {
    super(paramChar);
  }

  public boolean checkValidMove(String paramString1, String paramString2, Board paramBoard)
    throws MovementException
  {
    if (checkOnDiagonal(paramString1, paramString2))
    {
      if (clearDiagonal(paramString1, paramString2, paramBoard))
        if (!spaceIsEmpty(paramString2, paramBoard))
        {
          if (this.color == 'w')
          {
            if (paramBoard.getPieceAt(paramString2) < 0)
              return true;
          }
          else if ((this.color == 'b') && (paramBoard.getPieceAt(paramString2) > 0))
            return true;
        }
        else
          return true;
    }
    else if ((checkSameRankOrFile(paramString1, paramString2)) && (checkClearRankOrFile(paramString1, paramString2, paramBoard)))
      if (!spaceIsEmpty(paramString2, paramBoard))
      {
        if (this.color == 'w')
        {
          if (paramBoard.getPieceAt(paramString2) < 0)
            return true;
        }
        else if ((this.color == 'b') && (paramBoard.getPieceAt(paramString2) > 0))
          return true;
      }
      else
        return true;
    throw new MovementException("Queen");
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.Queen
 * JD-Core Version:    0.6.0
 */