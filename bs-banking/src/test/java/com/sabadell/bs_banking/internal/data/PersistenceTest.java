package com.sabadell.bs_banking.internal.data;

import com.sabadell.bs_banking.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE, sdk = 23)
public final class PersistenceTest {
  private Persistence persistence;
  private static final String expiryDate = "expiryDate", merchantIdentifier =
      "merchantIdentifier";

  @Before public void before() {
    persistence = new Persistence(RuntimeEnvironment.application);
  }

  @Test public void Verify_Persistence() {
    persistence.saveTokenCreditCardData(expiryDate, merchantIdentifier);

    assertThat(persistence.getExpiryDate(), is(expiryDate));
    assertThat(persistence.getMerchantIdentifier(), is(merchantIdentifier));

    persistence.clearCache();
    assertNull(persistence.getExpiryDate());
    assertNull(persistence.getMerchantIdentifier());
  }
}
