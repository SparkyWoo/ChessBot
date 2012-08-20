package edu.ucsd.cse70.ircclient;

public class Bishop extends ChessPiece
{
  public Bishop(char paramChar)
  {
    super(paramChar);
  }

  public boolean checkValidMove(String paramString1, String paramString2, Board paramBoard)
    throws MovementException
  {
    if ((checkOnDiagonal(paramString1, paramString2)) && (clearDiagonal(paramString1, paramString2, paramBoard)))
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
    throw new MovementException("Bishop");
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.Bishop
 * JD-Core Version:    0.6.0
 */