import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class POSTExample {

    public static void main(String[] args) {
        POSTExample postExample = new POSTExample("104.248.47.74", 80);
        postExample.post3RandomNumbers();
    }

    private String BASE_URL; // Base URL (address) of the server

    /**
     * Create an HTTP POST example
     *
     * @param host Will send request to this host: IP address or domain
     * @param port Will use this port
     */
    public POSTExample(String host, int port) {
        BASE_URL = "http://" + host + ":" + port + "/";
    }

    /**
     * Post three random numbers to a specific path on the web server
     */
    public void post3RandomNumbers() {
        int a = (int) Math.round(Math.random() * 100);
        int b = (int) Math.round(Math.random() * 100);
        int c = (int) Math.round(Math.random() * 100);

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append(" \"a\": ").append(a);
        json.append(", \"b\": ").append(b);
        json.append(", \"c\": ").append(c);
        json.append(" }");
        System.out.println("Posting this JSON data to server");
        System.out.println(json.toString());
        // TODO: change path to something correct
        sendPost("dkrest/auth", json.toString());
    }

    /**
     * Send HTTP POST
     *
     * @param path     Relative path in the API.
     * @param jsonData The data in JSON format that will be posted to the server
     */
    private void sendPost(String path, String jsonData) {
        try {
            String url = BASE_URL + path;
            URL urlObj = new URL(url);
            System.out.println("Sending HTTP POST to " + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(jsonData.getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server reached");

                // Response was OK, read the body (data)
                InputStream stream = con.getInputStream();
                String responseBody = convertStreamToString(stream);
                stream.close();
                System.out.println("Response from the server:");
                System.out.println(responseBody);
            } else {
                String responseDescription = con.getResponseMessage();
                System.out.println("Request failed, response code: " + responseCode + " (" + responseDescription + ")");
            }
        } catch (ProtocolException e) {
            System.out.println("Protocol nto supported by the server");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read the whole content from an InputStream, return it as a string
     * @param is
     * @return
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append('\n');
            }
        } catch (IOException ex) {
            System.out.println("Could not read the data from HTTP response: " + ex.getMessage());
        } finally {
        }
        return response.toString();
    }
}
