package io.github.undeffineddev.mcstatusio.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class JavaStatus {

    private static final String API_URL = "https://api.mcstatus.io/v2/status/java/";

    private JSONObject statusData;


    /**
     * Constructor to fetch the status of a Java server.
     *
     * @param address The server address.
     */
    public JavaStatus(String address) {
        this(address, true, 5.0);
    }

    /**
     * Constructor to fetch the status of a Java server with custom query and timeout settings.
     *
     * @param address The server address.
     * @param query   Whether to query the server for additional details.
     * @param timeout The timeout in seconds.
     */
    public JavaStatus(String address, boolean query, double timeout) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Server address cannot be null or empty");
        }

        try {
            String urlString = API_URL + address + "?query=" + query + "&timeout=" + timeout;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    statusData = new JSONObject(response.toString());
                }
            } else {
                throw new RuntimeException("Error: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if the server is online.
     *
     * @return True if the server is online, false otherwise.
     */
    public boolean isOnline() {
        return statusData != null && statusData.getBoolean("online");
    }

    /**
     * Gets the server's host address.
     *
     * @return The server's host address.
     */
    public String getHost() {
        return statusData.getString("host");
    }

    /**
     * Gets the server's port.
     *
     * @return The server's port.
     */
    public int getPort() {
        return statusData.getInt("port");
    }

    /**
     * Gets the server's IP address.
     *
     * @return The server's IP address, or null if not available.
     */
    public String getIpAddress() {
        return statusData.optString("ip_address", null);
    }

    /**
     * Checks if the server is blocked due to EULA violations.
     *
     * @return True if the server is EULA blocked, false otherwise.
     */
    public boolean isEulaBlocked() {
        return statusData.getBoolean("eula_blocked");
    }

    /**
     * Gets the timestamp when the data was retrieved.
     *
     * @return The timestamp when the data was retrieved.
     */
    public long getRetrievedAt() {
        return statusData.getLong("retrieved_at");
    }

    /**
     * Gets the timestamp when the data expires.
     *
     * @return The timestamp when the data expires.
     */
    public long getExpiresAt() {
        return statusData.getLong("expires_at");
    }

    /**
     * Gets the SRV record of the server.
     *
     * @return The SRV record, or null if not available.
     */
    public String getSrvRecord() {
        return statusData.optString("srv_record", null);
    }

    /**
     * Gets the raw version name of the server.
     *
     * @return The raw version name, or null if the server is offline.
     */
    public String getVersionNameRaw() {
        if (isOnline()) {
            return statusData.getJSONObject("version").getString("name_raw");
        }
        return null;
    }

    /**
     * Gets the clean version name of the server.
     *
     * @return The clean version name, or null if the server is offline.
     */
    public String getVersionNameClean() {
        if (isOnline()) {
            return statusData.getJSONObject("version").getString("name_clean");
        }
        return null;
    }

    /**
     * Gets the HTML version name of the server.
     *
     * @return The HTML version name, or null if the server is offline.
     */
    public String getVersionNameHtml() {
        if (isOnline()) {
            return statusData.getJSONObject("version").getString("name_html");
        }
        return null;
    }

    /**
     * Gets the protocol version of the server.
     *
     * @return The protocol version, or -1 if the server is offline.
     */
    public int getVersionProtocol() {
        if (isOnline()) {
            return statusData.getJSONObject("version").getInt("protocol");
        }
        return -1;
    }

    /**
     * Gets the number of players currently online.
     *
     * @return The number of players online, or 0 if the server is offline.
     */
    public int getPlayersOnline() {
        if (isOnline()) {
            return statusData.getJSONObject("players").getInt("online");
        }
        return 0;
    }

    /**
     * Gets the maximum number of players allowed on the server.
     *
     * @return The maximum number of players, or 0 if the server is offline.
     */
    public int getMaxPlayers() {
        if (isOnline()) {
            return statusData.getJSONObject("players").getInt("max");
        }
        return 0;
    }

    /**
     * Gets the list of players currently online.
     *
     * @return A JSONArray of players, or null if the server is offline or no players are online.
     */
    public JSONArray getPlayersList() {
        if (isOnline()) {
            return statusData.getJSONObject("players").optJSONArray("list");
        }
        return null;
    }

    /**
     * Gets the list of players currently online.
     *
     * @return A list of player names, or an empty list if the server is offline or no players are online.
     */
    public ArrayList<String> getPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        if (isOnline()) {
            JSONArray playersList = statusData.getJSONObject("players").optJSONArray("list");
            if (playersList != null) {
                for (int i = 0; i < playersList.length(); i++) {
                    JSONObject player = playersList.getJSONObject(i);
                    playerNames.add(player.getString("name_clean"));
                }
            }
        }
        return playerNames;
    }

    /**
     * Gets the list of players with their raw names.
     *
     * @return A list of player names with formatting, or an empty list if the server is offline or no players are online.
     */
    public ArrayList<String> getPlayerNamesRaw() {
        ArrayList<String> playerNames = new ArrayList<>();
        if (isOnline()) {
            JSONArray playersList = statusData.getJSONObject("players").optJSONArray("list");
            if (playersList != null) {
                for (int i = 0; i < playersList.length(); i++) {
                    JSONObject player = playersList.getJSONObject(i);
                    playerNames.add(player.getString("name_raw"));
                }
            }
        }
        return playerNames;
    }

    /**
     * Gets the raw MOTD (Message of the Day) of the server.
     *
     * @return The raw MOTD, or null if the server is offline.
     */
    public String getMotdRaw() {
        if (isOnline()) {
            return statusData.getJSONObject("motd").getString("raw");
        }
        return null;
    }

    /**
     * Gets the clean MOTD (Message of the Day) of the server.
     *
     * @return The clean MOTD, or null if the server is offline.
     */
    public String getMotdClean() {
        if (isOnline()) {
            return statusData.getJSONObject("motd").getString("clean");
        }
        return null;
    }

    /**
     * Gets the HTML MOTD (Message of the Day) of the server.
     *
     * @return The HTML MOTD, or null if the server is offline.
     */
    public String getMotdHtml() {
        if (isOnline()) {
            return statusData.getJSONObject("motd").getString("html");
        }
        return null;
    }

    /**
     * Gets the server's icon.
     *
     * @return The server's icon as a Base64 string, or null if the server is offline or no icon is available.
     */
    public String getIcon() {
        if (isOnline()) {
            return statusData.optString("icon", null);
        }
        return null;
    }

    /**
     * Gets the list of mods installed on the server.
     *
     * @return A JSONArray of mods, or null if the server is offline or no mods are installed.
     */
    public JSONArray getMods() {
        if (isOnline()) {
            return statusData.optJSONArray("mods");
        }
        return null;
    }

    /**
     * Gets the list of mods installed on the server.
     *
     * @return A list of mod names, or an empty list if the server is offline or no mods are installed.
     */
    public ArrayList<String> getModNames() {
        ArrayList<String> modNames = new ArrayList<>();
        if (isOnline()) {
            JSONArray modsList = statusData.optJSONArray("mods");
            if (modsList != null) {
                for (int i = 0; i < modsList.length(); i++) {
                    JSONObject mod = modsList.getJSONObject(i);
                    modNames.add(mod.getString("name"));
                }
            }
        }
        return modNames;
    }

    /**
     * Gets the list of mods with their versions.
     *
     * @return A map of mod names to their versions, or an empty map if the server is offline or no mods are installed.
     */
    public HashMap<String, String> getModsWithVersions() {
        HashMap<String, String> modsWithVersions = new HashMap<>();
        if (isOnline()) {
            JSONArray modsList = statusData.optJSONArray("mods");
            if (modsList != null) {
                for (int i = 0; i < modsList.length(); i++) {
                    JSONObject mod = modsList.getJSONObject(i);
                    modsWithVersions.put(mod.getString("name"), mod.getString("version"));
                }
            }
        }
        return modsWithVersions;
    }

    /**
     * Gets the software running on the server.
     *
     * @return The server software, or null if the server is offline or no software information is available.
     */
    public String getSoftware() {
        if (isOnline()) {
            return statusData.optString("software", null);
        }
        return null;
    }

    /**
     * Gets the list of plugins installed on the server.
     *
     * @return A JSONArray of plugins, or null if the server is offline or no plugins are installed.
     */
    public JSONArray getPlugins() {
        if (isOnline()) {
            return statusData.optJSONArray("plugins");
        }
        return null;
    }

    /**
     * Gets the list of plugins installed on the server.
     *
     * @return A list of plugin names, or an empty list if the server is offline or no plugins are installed.
     */
    public ArrayList<String> getPluginNames() {
        ArrayList<String> pluginNames = new ArrayList<>();
        if (isOnline()) {
            JSONArray pluginsList = statusData.optJSONArray("plugins");
            if (pluginsList != null) {
                for (int i = 0; i < pluginsList.length(); i++) {
                    JSONObject plugin = pluginsList.getJSONObject(i);
                    pluginNames.add(plugin.getString("name"));
                }
            }
        }
        return pluginNames;
    }

    /**
     * Gets the list of plugins with their versions.
     *
     * @return A map of plugin names to their versions, or an empty map if the server is offline or no plugins are installed.
     */
    public HashMap<String, String> getPluginsWithVersions() {
        HashMap<String, String> pluginsWithVersions = new HashMap<>();
        if (isOnline()) {
            JSONArray pluginsList = statusData.optJSONArray("plugins");
            if (pluginsList != null) {
                for (int i = 0; i < pluginsList.length(); i++) {
                    JSONObject plugin = pluginsList.getJSONObject(i);
                    pluginsWithVersions.put(plugin.getString("name"), plugin.optString("version", "Unknown"));
                }
            }
        }
        return pluginsWithVersions;
    }
}