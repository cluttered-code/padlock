package com.cluttered.code.saas.padlock.util;

import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * @author cluttered.code@gmail.com
 */
public final class PasswordHash {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordHash.class);
    private static final byte[] DEFAULT_SALT = generateSalt();

    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int SALT_LENGTH = 80;
    private static final int HASH_LENGTH = 64;
    private static final int PBKDF2_ITERATIONS = 64_000;

    public static byte[] hash(final String plaintext) {
        return hash(plaintext, generateSalt());
    }

    private static byte[] hash(final String plaintext, final byte[] salt) {
        LOG.debug("hashing password with salt");
        final char[] passwordChars = plaintext.toCharArray();
        try {
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            final PBEKeySpec keySpec = new PBEKeySpec(passwordChars, salt, PBKDF2_ITERATIONS, HASH_LENGTH);
            final SecretKey key = keyFactory.generateSecret(keySpec);
            final byte[] hash = key.getEncoded();
            return Bytes.concat(salt, hash);
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("unable to hash password for storage");
        }
    }

    private static byte[] generateSalt() {
        LOG.debug("generated salt");
        final SecureRandom random = new SecureRandom();
        final byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public static boolean validate(final String plaintext, final byte[] hash) {
        LOG.trace("validating password");
        final boolean isValidHash = hash != null && hash.length > 0;
        final byte[] salt = isValidHash ? Arrays.copyOf(hash, SALT_LENGTH) : DEFAULT_SALT;
        final byte[] newHash = hash(plaintext, salt);
        final byte[] secureHash = isValidHash ? hash : newHash;
        final boolean hashesAreEqual = verboseEquals(newHash, secureHash);
        return isValidHash && hashesAreEqual;
    }

    private static boolean verboseEquals(final byte[] hash1, final byte[] hash2) {
        boolean valid = hash1.length == hash2.length;
        for (int i = 0; i < hash1.length && i < hash2.length; ++i) {
            if (hash1[i] != hash2[i])
                valid = false;
        }
        return valid;
    }
}