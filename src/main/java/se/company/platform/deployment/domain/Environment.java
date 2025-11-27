package se.company.platform.deployment.domain;

public enum Environment {

    DEV, STST, ATST, PROD;

    public boolean isProdLike() {
        return this == PROD;
    }

    public boolean isNonProd() {
        return this != PROD;
    }
}
