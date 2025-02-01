package io.github.undeffineddev.mcstatusio.java.icon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Icon {

    private static final String API_URL = "https://api.mcstatus.io/v2/icon/";

    /**
     * Retrieves the server icon as a BufferedImage.
     *
     * @param address The server address.
     * @return The server icon as a BufferedImage, or null if an error occurs.
     */
    public static BufferedImage getServerIcon(String address) {
        return getServerIcon(address, 5.0);
    }

    /**
     * Retrieves the server icon as a BufferedImage with a specified timeout.
     *
     * @param address The server address.
     * @param timeout The timeout in seconds.
     * @return The server icon as a BufferedImage, or null if an error occurs.
     */
    public static BufferedImage getServerIcon(String address, double timeout) {
        try {
            String imageUrl = getServerIconAsLink(address, timeout);

            URL url = new URL(imageUrl);
            return ImageIO.read(url);
        } catch (IOException e) {
            System.err.println("Error downloading the image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves the URL of the server icon.
     *
     * @param address The server address.
     * @return The URL of the server icon.
     */
    public static String getServerIconAsLink(String address) {
        return getServerIconAsLink(address, 5.0);
    }

    /**
     * Retrieves the URL of the server icon with a specified timeout.
     *
     * @param address The server address.
     * @param timeout The timeout in seconds.
     * @return The URL of the server icon.
     */
    public static String getServerIconAsLink(String address, double timeout) {
        return API_URL + address + "?timeout=" + timeout;
    }
}