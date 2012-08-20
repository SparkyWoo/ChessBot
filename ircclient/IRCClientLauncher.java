package edu.ucsd.cse70.ircclient;

import javax.swing.JOptionPane;

class IRCClientLauncher
{
  public static void main(String[] paramArrayOfString)
  {
    IRCMessageHandler localIRCMessageHandler = new IRCMessageHandler();
    IRCConnectionHandler localIRCConnectionHandler = new IRCConnectionHandler();
    IRCConnection localIRCConnection = new IRCConnection(localIRCMessageHandler);
    localIRCConnectionHandler.setConnection(localIRCConnection);
    JOptionPane.showMessageDialog(null, "CHESSBOT Started!");
    setupConnection(localIRCConnection);
    localIRCConnectionHandler.connect();
  }

  private static void setupConnection(IRCConnection paramIRCConnection)
  {
    paramIRCConnection.setUser(System.getenv("IRCUSER"));
    if (paramIRCConnection.getUser() == null)
      paramIRCConnection.setUser(System.getenv("USER"));
    if (paramIRCConnection.getUser() == null)
      paramIRCConnection.setUser("ircuser");
    paramIRCConnection.setName(System.getenv("IRCNAME"));
    if (paramIRCConnection.getName() == null)
      paramIRCConnection.setName("TEAM3ISCOOL");
    paramIRCConnection.setPasswd(null);
    paramIRCConnection.setNick(System.getenv("IRCNICK"));
    if (paramIRCConnection.getNick() == null)
      paramIRCConnection.setNick("chessbot");
    paramIRCConnection.setHost(System.getenv("IRCSERVER"));
    if (paramIRCConnection.getHost() == null)
      paramIRCConnection.setHost("cse70.ucsd.edu");
    paramIRCConnection.setPort(6667);
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.IRCClientLauncher
 * JD-Core Version:    0.6.0
 */