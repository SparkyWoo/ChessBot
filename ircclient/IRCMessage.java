package edu.ucsd.cse70.ircclient;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IRCMessage
{
  public static final int MAX_LENGTH = 510;
  private static final Pattern messagePattern;
  private final Matcher matcher;
  private final String[] paramArray;
  private String _inputString;
  private static final int MESSAGE = 0;
  private static final int PREFIX = 1;
  private static final int PREFIX_SERVERNAME = 2;
  private static final int PREFIX_NICKNAME = 3;
  private static final int PREFIX_USER = 4;
  private static final int PREFIX_HOST = 5;
  private static final int COMMAND = 6;
  private static final int PARAMS = 7;

  public IRCMessage(String paramString)
  {
    this._inputString = paramString;
    this.matcher = messagePattern.matcher(paramString);
    if (this.matcher.matches())
      this.paramArray = parseParams(getPart(7));
    else
      this.paramArray = null;
    assert (this.paramArray.length <= 15);
  }

  public String getPrefix()
  {
    return getPart(1);
  }

  public String getServerName()
  {
    return getPart(2);
  }

  public String getNick()
  {
    return getPart(3);
  }

  public String getUser()
  {
    return getPart(4);
  }

  public String getHost()
  {
    return getPart(5);
  }

  public String getCommand()
  {
    return getPart(6);
  }

  public int getNumeric()
  {
    String str = getPart(6);
    if ((str != null) && (str.matches("[0-9]{3}")))
      return Integer.parseInt(getPart(6));
    return 0;
  }

  public String getParam(int paramInt)
  {
    int i = paramInt >= 0 ? paramInt : this.paramArray.length + paramInt;
    return this.paramArray != null ? this.paramArray[i] : null;
  }

  public String[] getParamArray()
  {
    return isValid() ? (String[])this.paramArray.clone() : null;
  }

  public int getNumParams()
  {
    return this.paramArray.length;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject != null) && ((paramObject instanceof IRCMessage)))
      return toString().equals(((IRCMessage)paramObject).toString());
    return false;
  }

  public String toString()
  {
    return this._inputString;
  }

  public boolean isValid()
  {
    return (this._inputString.length() <= 510) && (this.matcher.matches());
  }

  private String getPart(int paramInt)
  {
    return isValid() ? this.matcher.group(paramInt) : null;
  }

  private static String[] parseParams(String paramString)
  {
    Pattern localPattern = Pattern.compile("^ ((?:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]][:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*))");
    Matcher localMatcher = localPattern.matcher(paramString);
    Vector localVector = new Vector();
    while ((localVector.size() < 14) && (localMatcher.find()))
    {
      localVector.add(localMatcher.group(1));
      localMatcher.region(localMatcher.end(), localMatcher.regionEnd());
    }
    if (!localMatcher.hitEnd())
    {
      String str1 = paramString.substring(localMatcher.regionStart());
      assert (str1.matches(" :?(?:[: [\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)"));
      String str2 = newMethod(localVector.size(), str1);
      localVector.add(str2);
    }
    return (String[])(String[])localVector.toArray(new String[localVector.size()]);
  }

  private static String newMethod(int paramInt, String paramString)
  {
    String str;
    if (paramInt < 14)
    {
      assert (paramString.charAt(1) == ':');
      str = paramString.substring(2);
    }
    else if (paramString.charAt(1) == ':')
    {
      str = paramString.substring(2);
    }
    else
    {
      str = paramString.substring(1);
    }
    return str;
  }

  public String getChannel()
  {
    return getParam(0);
  }

  static
  {
    messagePattern = Pattern.compile("(?::(((?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*)|([A-Za-z[\\x5B-\\x60\\x7B-\\x7D]][A-Za-z0-9[\\x5B-\\x60\\x7B-\\x7D]-]{0,8})(?:(?:!([\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x40]]+))?@((?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*|(?:(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|(?:[0-9a-f]+(?::[0-9a-f]+){7}|0:0:0:0:0:(?:0|ffff):(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})))))?) )?([A-Za-z]+|[0-9]{3})(?:((?: (?:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]][:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)){0,14}(?: :(?:[: [\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*))?|(?: (?:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]][:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)){14}(?: :?(?:[: [\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*))?))?");
  }

  public static class Grammar
  {
    public static final String IP4ADDR_RE = "(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";
    public static final String IP6ADDR_RE = "(?:[0-9a-f]+(?::[0-9a-f]+){7}|0:0:0:0:0:(?:0|ffff):(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))";
    public static final String SHORTNAME_RE = "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)";
    public static final String HOSTNAME_RE = "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*";
    public static final String HOSTADDR_RE = "(?:(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|(?:[0-9a-f]+(?::[0-9a-f]+){7}|0:0:0:0:0:(?:0|ffff):(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})))";
    public static final String SERVERNAME_RE = "((?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*)";
    public static final String SPECIAL_RE = "[\\x5B-\\x60\\x7B-\\x7D]";
    public static final String NICKNAME_RE = "([A-Za-z[\\x5B-\\x60\\x7B-\\x7D]][A-Za-z0-9[\\x5B-\\x60\\x7B-\\x7D]-]{0,8})";
    public static final String USER_RE = "([\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x40]]+)";
    public static final String HOST_RE = "((?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*|(?:(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|(?:[0-9a-f]+(?::[0-9a-f]+){7}|0:0:0:0:0:(?:0|ffff):(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))))";
    public static final String NOSPCRLFCL_RE = "[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]";
    public static final String MIDDLE_RE = "(?:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]][:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)";
    public static final String TRAILING_RE = "(?:[: [\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)";
    public static final String PREFIX_RE = "(((?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*)|([A-Za-z[\\x5B-\\x60\\x7B-\\x7D]][A-Za-z0-9[\\x5B-\\x60\\x7B-\\x7D]-]{0,8})(?:(?:!([\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x40]]+))?@((?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*|(?:(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|(?:[0-9a-f]+(?::[0-9a-f]+){7}|0:0:0:0:0:(?:0|ffff):(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})))))?)";
    public static final String NUMERIC_RE = "[0-9]{3}";
    public static final String COMMAND_RE = "([A-Za-z]+|[0-9]{3})";
    public static final String PARAMS_RE = "((?: (?:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]][:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)){0,14}(?: :(?:[: [\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*))?|(?: (?:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]][:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)){14}(?: :?(?:[: [\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*))?)";
    public static final String MESSAGE_RE = "(?::(((?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*)|([A-Za-z[\\x5B-\\x60\\x7B-\\x7D]][A-Za-z0-9[\\x5B-\\x60\\x7B-\\x7D]-]{0,8})(?:(?:!([\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x40]]+))?@((?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)(?:\\.(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?))*|(?:(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|(?:[0-9a-f]+(?::[0-9a-f]+){7}|0:0:0:0:0:(?:0|ffff):(?:[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})))))?) )?([A-Za-z]+|[0-9]{3})(?:((?: (?:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]][:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)){0,14}(?: :(?:[: [\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*))?|(?: (?:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]][:[\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*)){14}(?: :?(?:[: [\\x01-\\xff&&[^\\x00\\x0A\\x0D\\x20\\x3A]]]*))?))?";
  }
}

/* Location:           C:\Users\user\Downloads\IRCClient.jar
 * Qualified Name:     edu.ucsd.cse70.ircclient.IRCMessage
 * JD-Core Version:    0.6.0
 */