package com.griddynamics.deming.data.generator.serialization;

import com.google.common.base.Objects;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
@RunWith(Theories.class)
public class JsonUtilTest {

    @DataPoints
    public static Object[][] data() {
        return new Object[][] {
                {
                        TestProduct.class,
                        "{}",
                        new TestProduct(null, null, 0.0)
                },
                {
                        TestProduct.class,
                        "{ \"id\": 9 }",
                        new TestProduct(9L, null, 0.0)
                },
                {
                        TestProduct.class,
                        "{ \"name\": \"Simple product\"}",
                        new TestProduct(null, "Simple product", 0.0)
                },
                {
                        TestProduct.class,
                        "{ \"price\": 9 }",
                        new TestProduct(null, null, 9.0)
                },
                {
                        TestProduct.class,
                        "{ \"id\": 10, \"name\": \"The best product\", \"price\": 46.23 }",
                        new TestProduct(10L, "The best product", 46.23)
                },
        };
    }

    @Theory
    public void test(final Object... testData) throws IOException {
        // Given
        Class serializedClass = (Class) testData[0];
        String json = (String) testData[1];
        Object expectedProduct = testData[2];

        InputStream inputStream = new ByteArrayInputStream(json.getBytes());

        // When
        Object result = JsonUtil.read(serializedClass, inputStream);

        // Then
        assertThat(result, equalTo(expectedProduct));
    }

    private static class TestProduct {
        public Long id;
        public String name;
        public double price;

        @SuppressWarnings("UnusedDeclaration")
        private TestProduct() {} // for serialization framework

        private TestProduct(Long id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }

            TestProduct that = (TestProduct) obj;

            return Objects.equal(this.id, that.id) &&
                    Objects.equal(this.name, that.name) &&
                    Objects.equal(this.price, that.price);
        }
    }
}
