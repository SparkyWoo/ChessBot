package edu.ucsd.cse70.ircclient;

public class King extends ChessPiece
{
  public King(char paramChar)
  {
    super(paramChar);
  }

  public void movePiece(String paramString1, String paramString2, IRCConnection paramIRCConnection, Board paramBoard)
    throws MovementException
  {
    super.movePiece(paramString1, paramString2, paramIRCConnection, paramBoard);
  }

  public boolean checkValidMove(String paramString1, String paramString2, Board paramBoard)
    throws MovementException
  {
    int i = paramString2.charAt(0) - paramString1.charAt(0);
    String str1 = "e1";
    String str2 = "h1";
    String str3 = "a1";
    String str4 = "h8";
    String str5 = "a8";
    String str6 = "e8";
    if (safeFromCheck(paramString2, paramBoard))
    {
      if ((paramString1.charAt(1) <= paramString2.charAt(1) + '\001') && (paramString1.charAt(1) >= paramString2.charAt(1) - '\001') && ((char)paramString1.charAt(0) <= (char)paramString2.charAt(0) + '\001') && ((char)paramString1.charAt(0) >= (char)paramString2.charAt(0) - '\001'))
      {
        if ((this.color == 'w') && (paramBoard.getPieceAt(paramString2) < 1))
          return true;
        if ((this.color == 'b') && (paramBoard.getPieceAt(paramString2) > -1))
          return true;
      }
      if (this.color == 'w')
      {
        if ((i == 2) && (!this.WKingMoved) && (safeFromCheck("f1", paramBoard)) && (checkClearRankOrFile(str1, str2, paramBoard)))
          return true;
        if ((i == -2) && (!this.WKingMoved) && (safeFromCheck("d1", paramBoard)) && (checkClearRankOrFile(str1, str3, paramBoard)))
          return true;
      }
      else if (this.color == 'b')
      {
        if ((i == 2) && (!this.BKingMoved) && (safeFromCheck("f8", paramBoard)) && (checkClearRankOrFile(str6, str4, paramBoard)))
          return true;
        if ((i == -2) && (!this.BKingMoved) && (safeFromCheck("d8", paramBoard)) && (checkClearRankOrFile(str6, str5, paramBoard)))
          return true;
      }
      throw new MovementException("King");
    }
    throw new MovementException("Check");
  }

  public boolean safeFromCheck(String paramString, Board paramBoard)
    throws MovementException
  {
    return spotNotThreatened(paramString, paramBoard);
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.King
 * JD-Core Version:    0.6.0
 */