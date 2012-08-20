package edu.ucsd.cse70.ircclient;

public class Knight extends ChessPiece
{
  int distanceVert = 0;
  int distanceHorz = 0;

  public Knight(char paramChar)
  {
    super(paramChar);
  }

  public boolean checkValidMove(String paramString1, String paramString2, Board paramBoard)
    throws MovementException
  {
    if (this.color == 'b')
    {
      if ((paramString2.charAt(0) != paramString1.charAt(0)) && (paramString2.charAt(1) != paramString1.charAt(1)))
      {
        this.distanceVert = (paramString1.charAt(1) - paramString2.charAt(1));
        this.distanceHorz = (paramString1.charAt(0) - paramString2.charAt(0));
        if ((this.distanceVert == 2) || (this.distanceVert == -2))
        {
          if (((this.distanceHorz == 1) || (this.distanceHorz == -1)) && (paramBoard.getPieceAt(paramString2) >= 0))
            return true;
        }
        else if (((this.distanceVert == 1) || (this.distanceVert == -1)) && ((this.distanceHorz == 2) || (this.distanceHorz == -2)) && (paramBoard.getPieceAt(paramString2) >= 0))
          return true;
      }
    }
    else if ((this.color == 'w') && (paramString2.charAt(0) != paramString1.charAt(0)) && (paramString2.charAt(1) != paramString1.charAt(1)))
    {
      this.distanceVert = (paramString1.charAt(1) - paramString2.charAt(1));
      this.distanceHorz = (paramString1.charAt(0) - paramString2.charAt(0));
      if ((this.distanceVert == 2) || (this.distanceVert == -2))
      {
        if (((this.distanceHorz == 1) || (this.distanceHorz == -1)) && (paramBoard.getPieceAt(paramString2) <= 0))
          return true;
      }
      else if (((this.distanceVert == 1) || (this.distanceVert == -1)) && ((this.distanceHorz == 2) || (this.distanceHorz == -2)) && (paramBoard.getPieceAt(paramString2) <= 0))
        return true;
    }
    throw new MovementException("Knight");
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.Knight
 * JD-Core Version:    0.6.0
 */