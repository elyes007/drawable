package tn.disguisedtoast.drawable.previewModule.models;

public class Device {
    private String name;
    private float width;
    private float height;
    private String userAgent;
    public static Device[] devices = {
            new Device("Pixel 2", 411, 821,
                    "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Mobile Safari/537.36",
                    new Skin(51, 28, 48, 21, "pixel2.png")),
            new Device("Galaxy S5", 360, 700,
                    "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Mobile Safari/537.36",
                    new Skin(63, 23, 63, 24, "s5.png")),
            new Device("Galaxy S9/S9+", 360, 725,
                    "Mozilla/5.0 (Linux; Android 7.0; SM-G892A Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/67.0.3396.87 Mobile Safari/537.36",
                    new Skin(35, 10, 35, 10, "s9.png")),
            new Device("IPad", 670, 1032,
                    "Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.34 (KHTML, like Gecko) Version/11.0 Mobile/15A5341f Safari/604.1",
                    new Skin(93, 35, 108, 35, "ipad.png")),
            new Device("IPhone 6/7/8", 370, 750,
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1",
                    new Skin(94, 34, 100, 32, "iphone6.png")),
            new Device("Iphone 9/7/8 Plus", 414, 800,
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1",
                    new Skin(94, 34, 100, 32, "iphone6.png")),
            new Device("IPhone X/XS", 375, 812,
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/12.0 Mobile/15A372 Safari/604.1",
                    new Skin(94, 34, 100, 32, "iphone6.png"))
    };
    private Skin skin;

    public Device(String name, float width, float height, String userAgent, Skin skin) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.userAgent = userAgent;
        this.skin = skin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    @Override
    public String toString() {
        return name;
    }
}
