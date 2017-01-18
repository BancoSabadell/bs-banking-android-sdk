package com.sabadell.bs_token.internal.data;

public final class Tx {
  private final String tx;

  public Tx(String tx) {
    this.tx = tx;
  }

  public String getValue() {
    return tx;
  }
}
