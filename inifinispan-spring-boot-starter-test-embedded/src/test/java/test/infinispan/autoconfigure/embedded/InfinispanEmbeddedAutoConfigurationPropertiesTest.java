package test.infinispan.autoconfigure.embedded;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.eviction.EvictionThreadPolicy;
import org.infinispan.manager.EmbeddedCacheManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import infinispan.autoconfigure.embedded.InfinispanEmbeddedAutoConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(InfinispanEmbeddedAutoConfiguration.class)
@TestPropertySource(properties = "infinispan.embedded.config-xml=infinispan-test-conf.xml")
public class InfinispanEmbeddedAutoConfigurationPropertiesTest {

    @Autowired
    EmbeddedCacheManager defaultCacheManager;

    @Test
    public void contextLoads() {

    }

    @Test
    public void testCacheManagerXmlConfig() {
        assertThat(defaultCacheManager.getCacheNames()).isEqualTo(Collections.singleton("default-local"));

        final GlobalConfiguration globalConfiguration = defaultCacheManager.getCacheManagerConfiguration();
        assertThat(globalConfiguration.globalJmxStatistics().enabled()).isTrue();
        assertThat(globalConfiguration.globalJmxStatistics().domain()).isEqualTo("properties.test.spring.infinispan");

        final Configuration defaultCacheConfiguration = defaultCacheManager.getDefaultCacheConfiguration();
        assertThat(defaultCacheConfiguration.eviction().maxEntries()).isEqualTo(2000L);
        assertThat(defaultCacheConfiguration.eviction().strategy()).isEqualTo(EvictionStrategy.LIRS);
        assertThat(defaultCacheConfiguration.eviction().threadPolicy()).isEqualTo(EvictionThreadPolicy.PIGGYBACK);
        assertThat(defaultCacheConfiguration.storeAsBinary().storeKeysAsBinary()).isTrue();
        assertThat(defaultCacheConfiguration.storeAsBinary().storeValuesAsBinary()).isTrue();
    }
}
