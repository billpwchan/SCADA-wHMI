package com.thalesgroup.scadagen.bps.conf.binding.engine;

/**
 * A composite key compsed of two strings
 */
class CompositeKey {

    /** first part of the key */
    private final String keyPart1_;
    
    /** second part of the key */
    private final String keyPart2_;

    /**
     * Constructor
     * @param keyPart1 first part of the key
     * @param keyPart2 second part of the key
     */
    public CompositeKey(String keyPart1, String keyPart2) {
        this.keyPart1_ = keyPart1;
        this.keyPart2_ = keyPart2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keyPart1_ == null) ? 0 : keyPart1_.hashCode());
        result = prime * result + ((keyPart2_ == null) ? 0 : keyPart2_.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompositeKey other = (CompositeKey) obj;
        if (keyPart1_ == null) {
            if (other.keyPart1_ != null) {
                return false;
            }
        } else if (!keyPart1_.equals(other.keyPart1_)) {
            return false;
        }
        if (keyPart2_ == null) {
            if (other.keyPart2_ != null) {
                return false;
            }
        } else if (!keyPart2_.equals(other.keyPart2_)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CompositeKey [" + keyPart1_ + "," + keyPart2_ + "]";
    }
    
}
