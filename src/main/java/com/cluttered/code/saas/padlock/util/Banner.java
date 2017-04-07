package com.cluttered.code.saas.padlock.util;

import com.google.common.io.Resources;
import org.slf4j.Logger;

import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author cluttered.code@gmail.com
 */
public class Banner {

    public static void display(final Logger log) {
        try {
            final URL bannerURL = Resources.getResource("banner.txt");
            final String banner = Resources.toString(bannerURL, StandardCharsets.UTF_8)
                    .replaceAll("\\r\\n?", "\n");
            log.info(banner);
        } catch (final Throwable t) {
            // No Banner to Display
        }
    }
}
