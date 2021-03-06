
package http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import constant.HTTP;


public enum HttpVersion
{
	/**
	 * HTTP/1.0
	 */
	VERSION_1_0(1, 0),

	/**
	 * HTTP/1.1
	 */
	VERSION_1_1(1, 1);

	private final int major;
	private final int minor;

	private HttpVersion(int major, int minor)
	{
		this.major = major;
		this.minor = minor;
	}

	@Override
	public String toString()
	{
		return HTTP.HTTP + HTTP.PROTOCOL_DELIMITER + major + "." + minor;
	}

	
	public static HttpVersion extractVersion(String headerLine) throws IllegalArgumentException
	{
		Matcher m = Pattern.compile(HTTP.HTTP + "/(\\d+)\\.(\\d+)").matcher(headerLine);
		if (m.find())
		{
			if ((Integer.parseInt(m.group(1)) == 1) && (Integer.parseInt(m.group(2)) == 1))
			{
				return VERSION_1_1;
			}
			else if ((Integer.parseInt(m.group(1)) == 1) && (Integer.parseInt(m.group(2)) == 0))
			{
				return VERSION_1_0;
			}
			else
			{
				throw new IllegalArgumentException("Unknown HTTP Version");
			}
		}
		else
		{
			throw new IllegalArgumentException("Unknown HTTP Version");
		}
	}
}
