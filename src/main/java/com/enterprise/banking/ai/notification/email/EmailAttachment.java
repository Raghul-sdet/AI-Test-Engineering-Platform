package com.enterprise.banking.ai.notification.email;

/**
 * Represents a file attachment bounded to an email payload.
 */
public class EmailAttachment {
    private final String fileName;
    private final byte[] content;
    private final String mimeType;

    public EmailAttachment(String fileName, byte[] content, String mimeType) {
        this.fileName = fileName;
        this.content = content.clone();
        this.mimeType = mimeType;
    }

    public String getFileName() { return fileName; }
    public byte[] getContent() { return content.clone(); }
    public String getMimeType() { return mimeType; }
}