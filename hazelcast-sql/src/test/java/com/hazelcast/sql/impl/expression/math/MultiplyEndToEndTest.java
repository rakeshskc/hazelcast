/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.sql.impl.expression.math;

import com.hazelcast.sql.impl.expression.ExpressionEndToEndTestBase;
import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.annotation.ParallelJVMTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.hazelcast.sql.SqlColumnType.BIGINT;
import static com.hazelcast.sql.SqlColumnType.DECIMAL;
import static com.hazelcast.sql.SqlColumnType.DOUBLE;
import static com.hazelcast.sql.SqlColumnType.INTEGER;
import static com.hazelcast.sql.SqlColumnType.REAL;
import static com.hazelcast.sql.SqlColumnType.SMALLINT;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelJVMTest.class})
public class MultiplyEndToEndTest extends ExpressionEndToEndTestBase {

    @Test
    public void testBoolean() {
        assertParsingError("booleanTrue * booleanTrue", "Cannot apply '*' to arguments of type '<BOOLEAN> * <BOOLEAN>'");

        assertParsingError("booleanTrue * byte1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <TINYINT>'");
        assertParsingError("booleanTrue * short1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <SMALLINT>'");
        assertParsingError("booleanTrue * int1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <INTEGER>'");
        assertParsingError("booleanTrue * long1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <BIGINT>'");

        assertParsingError("booleanTrue * float1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <REAL>'");
        assertParsingError("booleanTrue * double1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <DOUBLE>'");

        assertParsingError("booleanTrue * decimal1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <DECIMAL(38, 38)>'");
        assertParsingError("booleanTrue * bigInteger1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <DECIMAL(38, 38)>'");

        assertParsingError("booleanTrue * string1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <BOOLEAN>'");
        assertParsingError("booleanTrue * char1", "Cannot apply '*' to arguments of type '<BOOLEAN> * <BOOLEAN>'");

        assertParsingError("booleanTrue * object", "Cannot apply '*' to arguments of type '<BOOLEAN> * <OBJECT>'");
    }

    @Test
    public void testTinyint() {
        assertParsingError("byte1 * booleanTrue", "Cannot apply '*' to arguments of type '<TINYINT> * <BOOLEAN>'");

        assertRow("byte1 * byte1", EXPR0, SMALLINT, (short) 1);
        assertRow("byte1 * byteMax", EXPR0, SMALLINT, (short) Byte.MAX_VALUE);
        assertRow("byteMax * byteMax", EXPR0, SMALLINT, (short) (Byte.MAX_VALUE * Byte.MAX_VALUE));

        assertRow("byte1 * short1", EXPR0, INTEGER, 1);
        assertRow("byte1 * shortMax", EXPR0, INTEGER, (int) Short.MAX_VALUE);
        assertRow("byteMax * shortMax", EXPR0, INTEGER, Byte.MAX_VALUE * Short.MAX_VALUE);

        assertRow("byte1 * int1", EXPR0, BIGINT, 1L);
        assertRow("byte1 * intMax", EXPR0, BIGINT, (long) Integer.MAX_VALUE);
        assertRow("byteMax * intMax", EXPR0, BIGINT, (long) Byte.MAX_VALUE * Integer.MAX_VALUE);

        assertRow("byte1 * long1", EXPR0, BIGINT, 1L);
        assertRow("byte1 * longMax", EXPR0, BIGINT, Long.MAX_VALUE);
        assertDataError("byteMax * longMax", "BIGINT overflow");

        assertRow("byte1 * float1", EXPR0, REAL, 1.0f);
        assertRow("byte1 * floatMax", EXPR0, REAL, Float.MAX_VALUE);
        assertRow("byteMax * float2", EXPR0, REAL, Byte.MAX_VALUE * 2.0f);
        //noinspection NumericOverflow
        assertRow("byteMax * floatMax", EXPR0, REAL, Byte.MAX_VALUE * Float.MAX_VALUE);

        assertRow("byte1 * double1", EXPR0, DOUBLE, 1.0);
        assertRow("byte1 * doubleMax", EXPR0, DOUBLE, Double.MAX_VALUE);
        assertRow("byteMax * double2", EXPR0, DOUBLE, Byte.MAX_VALUE * 2.0);
        //noinspection NumericOverflow
        assertRow("byteMax * doubleMax", EXPR0, DOUBLE, Byte.MAX_VALUE * Double.MAX_VALUE);

        assertRow("byte1 * decimal1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("byte1 * decimalBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("byteMax * decimalBig", EXPR0, DECIMAL, BigDecimal.valueOf(Byte.MAX_VALUE).multiply(getRecord().decimalBig));

        assertRow("byte1 * bigInteger1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("byte1 * bigIntegerBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("byteMax * bigIntegerBig", EXPR0, DECIMAL, BigDecimal.valueOf(Byte.MAX_VALUE).multiply(getRecord().decimalBig));

        assertRow("byte1 * string1", EXPR0, BIGINT, 1L);
        assertRow("byteMax * string2", EXPR0, BIGINT, Byte.MAX_VALUE * 2L);
        assertDataError("byte1 * stringBig", "Cannot convert VARCHAR to BIGINT");
        assertDataError("byte1 * stringFoo", "Cannot convert VARCHAR to BIGINT");
        assertRow("byte1 * char1", EXPR0, BIGINT, 1L);
        assertDataError("byte1 * charF", "Cannot convert VARCHAR to BIGINT");

        assertParsingError("byte1 * object", "Cannot apply '*' to arguments of type '<TINYINT> * <OBJECT>'");
    }

    @Test
    public void testSmallint() {
        assertParsingError("short1 * booleanTrue", "Cannot apply '*' to arguments of type '<SMALLINT> * <BOOLEAN>'");

        assertRow("short1 * byte1", EXPR0, INTEGER, 1);
        assertRow("short1 * byteMax", EXPR0, INTEGER, (int) Byte.MAX_VALUE);
        assertRow("shortMax * byteMax", EXPR0, INTEGER, Byte.MAX_VALUE * Short.MAX_VALUE);

        assertRow("short1 * short1", EXPR0, INTEGER, 1);
        assertRow("short1 * shortMax", EXPR0, INTEGER, (int) Short.MAX_VALUE);
        assertRow("shortMax * shortMax", EXPR0, INTEGER, Short.MAX_VALUE * Short.MAX_VALUE);

        assertRow("short1 * int1", EXPR0, BIGINT, 1L);
        assertRow("short1 * intMax", EXPR0, BIGINT, (long) Integer.MAX_VALUE);
        assertRow("shortMax * intMax", EXPR0, BIGINT, (long) Short.MAX_VALUE * Integer.MAX_VALUE);

        assertRow("short1 * long1", EXPR0, BIGINT, 1L);
        assertRow("short1 * longMax", EXPR0, BIGINT, Long.MAX_VALUE);
        assertDataError("shortMax * longMax", "BIGINT overflow");

        assertRow("short1 * float1", EXPR0, REAL, 1.0f);
        assertRow("short1 * floatMax", EXPR0, REAL, Float.MAX_VALUE);
        assertRow("shortMax * float2", EXPR0, REAL, Short.MAX_VALUE * 2.0f);
        //noinspection NumericOverflow
        assertRow("shortMax * floatMax", EXPR0, REAL, Short.MAX_VALUE * Float.MAX_VALUE);

        assertRow("short1 * double1", EXPR0, DOUBLE, 1.0);
        assertRow("short1 * doubleMax", EXPR0, DOUBLE, Double.MAX_VALUE);
        assertRow("shortMax * double2", EXPR0, DOUBLE, Short.MAX_VALUE * 2.0);
        //noinspection NumericOverflow
        assertRow("shortMax * doubleMax", EXPR0, DOUBLE, Short.MAX_VALUE * Double.MAX_VALUE);

        assertRow("short1 * decimal1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("short1 * decimalBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("shortMax * decimalBig", EXPR0, DECIMAL, BigDecimal.valueOf(Short.MAX_VALUE).multiply(getRecord().decimalBig));

        assertRow("short1 * bigInteger1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("short1 * bigIntegerBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("shortMax * bigIntegerBig", EXPR0, DECIMAL,
                BigDecimal.valueOf(Short.MAX_VALUE).multiply(getRecord().decimalBig));

        assertRow("short1 * string1", EXPR0, BIGINT, 1L);
        assertRow("shortMax * string2", EXPR0, BIGINT, Short.MAX_VALUE * 2L);
        assertDataError("short1 * stringBig", "Cannot convert VARCHAR to BIGINT");
        assertDataError("short1 * stringFoo", "Cannot convert VARCHAR to BIGINT");
        assertRow("short1 * char1", EXPR0, BIGINT, 1L);
        assertDataError("short1 * charF", "Cannot convert VARCHAR to BIGINT");

        assertParsingError("short1 * object", "Cannot apply '*' to arguments of type '<SMALLINT> * <OBJECT>'");
    }

    @Test
    public void testInteger() {
        assertParsingError("int1 * booleanTrue", "Cannot apply '*' to arguments of type '<INTEGER> * <BOOLEAN>'");

        assertRow("int1 * byte1", EXPR0, BIGINT, 1L);
        assertRow("int1 * byteMax", EXPR0, BIGINT, (long) Byte.MAX_VALUE);
        assertRow("intMax * byteMax", EXPR0, BIGINT, (long) Integer.MAX_VALUE * Byte.MAX_VALUE);

        assertRow("int1 * short1", EXPR0, BIGINT, 1L);
        assertRow("int1 * shortMax", EXPR0, BIGINT, (long) Short.MAX_VALUE);
        assertRow("intMax * shortMax", EXPR0, BIGINT, (long) Integer.MAX_VALUE * Short.MAX_VALUE);

        assertRow("int1 * int1", EXPR0, BIGINT, 1L);
        assertRow("int1 * intMax", EXPR0, BIGINT, (long) Integer.MAX_VALUE);
        assertRow("intMax * intMax", EXPR0, BIGINT, (long) Integer.MAX_VALUE * Integer.MAX_VALUE);

        assertRow("int1 * long1", EXPR0, BIGINT, 1L);
        assertRow("intMax * long1", EXPR0, BIGINT, (long) Integer.MAX_VALUE);
        assertDataError("intMax * longMax", "BIGINT overflow");

        assertRow("int1 * float1", EXPR0, REAL, 1.0f);
        assertRow("int1 * floatMax", EXPR0, REAL, Float.MAX_VALUE);
        assertRow("intMax * float2", EXPR0, REAL, Integer.MAX_VALUE * 2.0f);
        //noinspection NumericOverflow
        assertRow("intMax * floatMax", EXPR0, REAL, Integer.MAX_VALUE * Float.MAX_VALUE);

        assertRow("int1 * double1", EXPR0, DOUBLE, 1.0);
        assertRow("int1 * doubleMax", EXPR0, DOUBLE, Double.MAX_VALUE);
        assertRow("intMax * double2", EXPR0, DOUBLE, Integer.MAX_VALUE * 2.0);
        //noinspection NumericOverflow
        assertRow("intMax * doubleMax", EXPR0, DOUBLE, Integer.MAX_VALUE * Double.MAX_VALUE);

        assertRow("int1 * decimal1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("int1 * decimalBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("intMax * decimalBig", EXPR0, DECIMAL, BigDecimal.valueOf(Integer.MAX_VALUE).multiply(getRecord().decimalBig));

        assertRow("int1 * bigInteger1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("int1 * bigIntegerBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("intMax * bigIntegerBig", EXPR0, DECIMAL,
                BigDecimal.valueOf(Integer.MAX_VALUE).multiply(getRecord().decimalBig));

        assertRow("int1 * string1", EXPR0, BIGINT, 1L);
        assertRow("intMax * string2", EXPR0, BIGINT, Integer.MAX_VALUE * 2L);
        assertDataError("int1 * stringBig", "Cannot convert VARCHAR to BIGINT");
        assertDataError("int1 * stringFoo", "Cannot convert VARCHAR to BIGINT");
        assertRow("int1 * char1", EXPR0, BIGINT, 1L);
        assertDataError("int1 * charF", "Cannot convert VARCHAR to BIGINT");

        assertParsingError("int1 * object", "Cannot apply '*' to arguments of type '<INTEGER> * <OBJECT>'");
    }

    @Test
    public void testBigint() {
        assertParsingError("long1 * booleanTrue", "Cannot apply '*' to arguments of type '<BIGINT> * <BOOLEAN>'");

        assertRow("long1 * byte1", EXPR0, BIGINT, 1L);
        assertRow("long1 * byteMax", EXPR0, BIGINT, (long) Byte.MAX_VALUE);
        assertDataError("longMax * byteMax", "BIGINT overflow");

        assertRow("long1 * short1", EXPR0, BIGINT, 1L);
        assertRow("long1 * shortMax", EXPR0, BIGINT, (long) Short.MAX_VALUE);
        assertDataError("longMax * shortMax", "BIGINT overflow");

        assertRow("long1 * int1", EXPR0, BIGINT, 1L);
        assertRow("long1 * intMax", EXPR0, BIGINT, (long) Integer.MAX_VALUE);
        assertDataError("longMax * intMax", "BIGINT overflow");

        assertRow("long1 * long1", EXPR0, BIGINT, 1L);
        assertRow("long1 * longMax", EXPR0, BIGINT, Long.MAX_VALUE);
        assertDataError("longMax * longMax", "BIGINT overflow");

        assertRow("long1 * float1", EXPR0, REAL, 1.0f);
        assertRow("long1 * floatMax", EXPR0, REAL, Float.MAX_VALUE);
        assertRow("longMax * float2", EXPR0, REAL, Long.MAX_VALUE * 2.0f);
        //noinspection NumericOverflow
        assertRow("longMax * floatMax", EXPR0, REAL, Long.MAX_VALUE * Float.MAX_VALUE);

        assertRow("long1 * double1", EXPR0, DOUBLE, 1.0);
        assertRow("long1 * doubleMax", EXPR0, DOUBLE, Double.MAX_VALUE);
        assertRow("longMax * double2", EXPR0, DOUBLE, Long.MAX_VALUE * 2.0);
        //noinspection NumericOverflow
        assertRow("longMax * doubleMax", EXPR0, DOUBLE, Long.MAX_VALUE * Double.MAX_VALUE);

        assertRow("long1 * decimal1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("long1 * decimalBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("longMax * decimalBig", EXPR0, DECIMAL,
                BigDecimal.valueOf(Long.MAX_VALUE).multiply(getRecord().decimalBig, ExpressionMath.DECIMAL_MATH_CONTEXT));

        assertRow("long1 * bigInteger1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("long1 * bigIntegerBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("longMax * bigIntegerBig", EXPR0, DECIMAL,
                BigDecimal.valueOf(Long.MAX_VALUE).multiply(getRecord().decimalBig, ExpressionMath.DECIMAL_MATH_CONTEXT));

        assertRow("long1 * string1", EXPR0, BIGINT, 1L);
        assertDataError("longMax * string2", "BIGINT overflow");
        assertDataError("long1 * stringBig", "Cannot convert VARCHAR to BIGINT");
        assertDataError("long1 * stringFoo", "Cannot convert VARCHAR to BIGINT");
        assertRow("long1 * char1", EXPR0, BIGINT, 1L);
        assertDataError("long1 * charF", "Cannot convert VARCHAR to BIGINT");

        assertParsingError("long1 * object", "Cannot apply '*' to arguments of type '<BIGINT> * <OBJECT>'");
    }

    @Test
    public void testReal() {
        assertParsingError("float1 * booleanTrue", "Cannot apply '*' to arguments of type '<REAL> * <BOOLEAN>'");

        assertRow("float1 * byte1", EXPR0, REAL, 1.0f);
        assertRow("float1 * byteMax", EXPR0, REAL, (float) Byte.MAX_VALUE);
        assertRow("float2 * byteMax", EXPR0, REAL, 2.0f * Byte.MAX_VALUE);

        assertRow("float1 * short1", EXPR0, REAL, 1.0f);
        assertRow("float1 * shortMax", EXPR0, REAL, (float) Short.MAX_VALUE);
        assertRow("float2 * shortMax", EXPR0, REAL, 2.0f * Short.MAX_VALUE);

        assertRow("float1 * int1", EXPR0, REAL, 1.0f);
        assertRow("float1 * intMax", EXPR0, REAL, (float) Integer.MAX_VALUE);
        assertRow("float2 * intMax", EXPR0, REAL, 2.0f * Integer.MAX_VALUE);

        assertRow("float1 * long1", EXPR0, REAL, 1.0f);
        assertRow("float1 * longMax", EXPR0, REAL, (float) Long.MAX_VALUE);
        assertRow("float2 * longMax", EXPR0, REAL, 2.0f * Long.MAX_VALUE);

        assertRow("float1 * float1", EXPR0, REAL, 1.0f);
        assertRow("float1 * floatMax", EXPR0, REAL, Float.MAX_VALUE);
        //noinspection NumericOverflow
        assertRow("float2 * floatMax", EXPR0, REAL, 2.0f * Float.MAX_VALUE);

        assertRow("float1 * double1", EXPR0, DOUBLE, 1.0);
        assertRow("float1 * doubleMax", EXPR0, DOUBLE, Double.MAX_VALUE);
        //noinspection NumericOverflow
        assertRow("float2 * doubleMax", EXPR0, DOUBLE, 2.0f * Double.MAX_VALUE);

        assertRow("float1 * decimal1", EXPR0, REAL, 1.0f);
        assertRow("float1 * decimalBig", EXPR0, REAL, getRecord().decimalBig.floatValue());
        assertRow("float2 * decimalBig", EXPR0, REAL, 2.0f * getRecord().decimalBig.floatValue());

        assertRow("float1 * bigInteger1", EXPR0, REAL, 1.0f);
        assertRow("float1 * bigIntegerBig", EXPR0, REAL, getRecord().bigIntegerBig.floatValue());
        assertRow("float2 * bigIntegerBig", EXPR0, REAL, 2.0f * getRecord().decimalBig.floatValue());

        assertRow("float1 * string1", EXPR0, REAL, 1.0f);
        assertRow("float2 * string2", EXPR0, REAL, 4.0f);
        assertRow("float1 * stringBig", EXPR0, REAL, Float.parseFloat(getRecord().stringBig));
        assertDataError("float1 * stringFoo", "Cannot convert VARCHAR to REAL");
        assertRow("float1 * char1", EXPR0, REAL, 1.0f);
        assertDataError("float1 * charF", "Cannot convert VARCHAR to REAL");

        assertParsingError("float1 * object", "Cannot apply '*' to arguments of type '<REAL> * <OBJECT>'");
    }

    @Test
    public void testDouble() {
        assertParsingError("double1 * booleanTrue", "Cannot apply '*' to arguments of type '<DOUBLE> * <BOOLEAN>'");

        assertRow("double1 * byte1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * byteMax", EXPR0, DOUBLE, (double) Byte.MAX_VALUE);
        assertRow("double2 * byteMax", EXPR0, DOUBLE, 2.0 * Byte.MAX_VALUE);

        assertRow("double1 * short1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * shortMax", EXPR0, DOUBLE, (double) Short.MAX_VALUE);
        assertRow("double2 * shortMax", EXPR0, DOUBLE, 2.0 * Short.MAX_VALUE);

        assertRow("double1 * int1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * intMax", EXPR0, DOUBLE, (double) Integer.MAX_VALUE);
        assertRow("double2 * intMax", EXPR0, DOUBLE, 2.0 * Integer.MAX_VALUE);

        assertRow("double1 * long1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * longMax", EXPR0, DOUBLE, (double) Long.MAX_VALUE);
        assertRow("double2 * longMax", EXPR0, DOUBLE, 2.0 * Long.MAX_VALUE);

        assertRow("double1 * float1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * floatMax", EXPR0, DOUBLE, (double) Float.MAX_VALUE);
        assertRow("double2 * floatMax", EXPR0, DOUBLE, 2.0 * Float.MAX_VALUE);

        assertRow("double1 * double1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * doubleMax", EXPR0, DOUBLE, Double.MAX_VALUE);
        //noinspection NumericOverflow
        assertRow("double2 * doubleMax", EXPR0, DOUBLE, 2.0 * Double.MAX_VALUE);

        assertRow("double1 * decimal1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * decimalBig", EXPR0, DOUBLE, getRecord().decimalBig.doubleValue());

        assertRow("double1 * bigInteger1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * bigIntegerBig", EXPR0, DOUBLE, getRecord().bigIntegerBig.doubleValue());

        assertRow("double1 * string1", EXPR0, DOUBLE, 1.0);
        assertRow("double1 * stringBig", EXPR0, DOUBLE, Double.parseDouble(getRecord().stringBig));
        assertRow("double2 * stringBig", EXPR0, DOUBLE, 2.0 * Double.parseDouble(getRecord().stringBig));
        assertDataError("double1 * stringFoo", "Cannot convert VARCHAR to DOUBLE");
        assertRow("double1 * char1", EXPR0, DOUBLE, 1.0);
        assertDataError("double1 * charF", "Cannot convert VARCHAR to DOUBLE");

        assertParsingError("double1 * object", "Cannot apply '*' to arguments of type '<DOUBLE> * <OBJECT>'");
    }

    @Test
    public void testDecimal() {
        assertParsingError("decimal1 * booleanTrue", "Cannot apply '*' to arguments of type '<DECIMAL(38, 38)> * <BOOLEAN>'");

        assertRow("decimal1 * byte1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("decimal1 * byteMax", EXPR0, DECIMAL, BigDecimal.valueOf(Byte.MAX_VALUE));
        assertRow("decimal2 * byteMax", EXPR0, DECIMAL, BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Byte.MAX_VALUE)));

        assertRow("decimal1 * short1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("decimal1 * shortMax", EXPR0, DECIMAL, BigDecimal.valueOf(Short.MAX_VALUE));
        assertRow("decimal2 * shortMax", EXPR0, DECIMAL, BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Short.MAX_VALUE)));

        assertRow("decimal1 * int1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("decimal1 * intMax", EXPR0, DECIMAL, BigDecimal.valueOf(Integer.MAX_VALUE));
        assertRow("decimal2 * intMax", EXPR0, DECIMAL, BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Integer.MAX_VALUE)));

        assertRow("decimal1 * long1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("decimal1 * longMax", EXPR0, DECIMAL, BigDecimal.valueOf(Long.MAX_VALUE));
        assertRow("decimal2 * longMax", EXPR0, DECIMAL, BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(Long.MAX_VALUE)));

        assertRow("decimal1 * float1", EXPR0, REAL, 1.0f);
        assertRow("decimal1 * floatMax", EXPR0, REAL, Float.MAX_VALUE);
        //noinspection NumericOverflow
        assertRow("decimal2 * floatMax", EXPR0, REAL, 2.0f * Float.MAX_VALUE);

        assertRow("decimal1 * double1", EXPR0, DOUBLE, 1.0);
        assertRow("decimal1 * doubleMax", EXPR0, DOUBLE, Double.MAX_VALUE);
        //noinspection NumericOverflow
        assertRow("decimal2 * doubleMax", EXPR0, DOUBLE, 2.0 * Double.MAX_VALUE);

        assertRow("decimal1 * decimal1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("decimal1 * decimalBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("decimal2 * decimalBig", EXPR0, DECIMAL, BigDecimal.valueOf(2).multiply(getRecord().decimalBig));

        assertRow("decimal1 * bigInteger1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("decimal1 * bigIntegerBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("decimal2 * bigIntegerBig", EXPR0, DECIMAL, BigDecimal.valueOf(2).multiply(getRecord().decimalBig));

        assertRow("decimal1 * string1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("decimal2 * string2", EXPR0, DECIMAL, BigDecimal.valueOf(4));
        assertRow("decimal1 * stringBig", EXPR0, DECIMAL, new BigDecimal(getRecord().stringBig));
        assertDataError("decimal1 * stringFoo", "Cannot convert VARCHAR to DECIMAL");
        assertRow("decimal1 * char1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertDataError("decimal1 * charF", "Cannot convert VARCHAR to DECIMAL");

        assertParsingError("decimal1 * object", "Cannot apply '*' to arguments of type '<DECIMAL(38, 38)> * <OBJECT>'");
    }

    @Test
    public void testVarchar() {
        assertParsingError("string1 * booleanTrue", "Cannot apply '*' to arguments of type '<BOOLEAN> * <BOOLEAN>'");

        assertRow("string1 * byte1", EXPR0, BIGINT, 1L);
        assertRow("string1 * byteMax", EXPR0, BIGINT, (long) Byte.MAX_VALUE);
        assertRow("string2 * byteMax", EXPR0, BIGINT, 2L * Byte.MAX_VALUE);

        assertRow("string1 * short1", EXPR0, BIGINT, 1L);
        assertRow("string1 * shortMax", EXPR0, BIGINT, (long) Short.MAX_VALUE);
        assertRow("string2 * shortMax", EXPR0, BIGINT, 2L * Short.MAX_VALUE);

        assertRow("string1 * int1", EXPR0, BIGINT, 1L);
        assertRow("string1 * intMax", EXPR0, BIGINT, (long) Integer.MAX_VALUE);
        assertRow("string2 * intMax", EXPR0, BIGINT, 2L * Integer.MAX_VALUE);

        assertRow("string1 * long1", EXPR0, BIGINT, 1L);
        assertRow("string1 * longMax", EXPR0, BIGINT, Long.MAX_VALUE);
        assertDataError("string2 * longMax", "BIGINT overflow");

        assertRow("string1 * float1", EXPR0, REAL, 1.0f);
        assertRow("string1 * floatMax", EXPR0, REAL, Float.MAX_VALUE);
        //noinspection NumericOverflow
        assertRow("string2 * floatMax", EXPR0, REAL, 2L * Float.MAX_VALUE);

        assertRow("string1 * double1", EXPR0, DOUBLE, 1.0);
        assertRow("string1 * doubleMax", EXPR0, DOUBLE, Double.MAX_VALUE);
        //noinspection NumericOverflow
        assertRow("string2 * doubleMax", EXPR0, DOUBLE, 2L * Double.MAX_VALUE);

        assertRow("string1 * decimal1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("string1 * decimalBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("string2 * decimalBig", EXPR0, DECIMAL, BigDecimal.valueOf(2).multiply(getRecord().decimalBig));

        assertRow("string1 * bigInteger1", EXPR0, DECIMAL, BigDecimal.valueOf(1));
        assertRow("string1 * bigIntegerBig", EXPR0, DECIMAL, getRecord().decimalBig);
        assertRow("string2 * bigIntegerBig", EXPR0, DECIMAL, BigDecimal.valueOf(2).multiply(getRecord().decimalBig));

        assertRow("string1 * string1", EXPR0, DOUBLE, 1.0);
        assertRow("string2 * string2", EXPR0, DOUBLE, 4.0);
        assertRow("string1 * stringBig", EXPR0, DOUBLE, Double.parseDouble(getRecord().stringBig));
        assertDataError("string1 * stringFoo", "Cannot convert VARCHAR to DOUBLE");
        assertRow("string1 * char1", EXPR0, DOUBLE, 1.0);
        assertDataError("string1 * charF", "Cannot convert VARCHAR to DOUBLE");

        assertParsingError("string1 * object", "Cannot apply '*' to arguments of type '<VARCHAR> * <OBJECT>'");
    }

    @Test
    public void testObject() {
        assertParsingError("object * booleanTrue", "Cannot apply '*' to arguments of type '<OBJECT> * <BOOLEAN>'");

        assertParsingError("object * byte1", "Cannot apply '*' to arguments of type '<OBJECT> * <TINYINT>'");
        assertParsingError("object * short1", "Cannot apply '*' to arguments of type '<OBJECT> * <SMALLINT>'");
        assertParsingError("object * int1", "Cannot apply '*' to arguments of type '<OBJECT> * <INTEGER>'");
        assertParsingError("object * long1", "Cannot apply '*' to arguments of type '<OBJECT> * <BIGINT>'");

        assertParsingError("object * float1", "Cannot apply '*' to arguments of type '<OBJECT> * <REAL>'");
        assertParsingError("object * double1", "Cannot apply '*' to arguments of type '<OBJECT> * <DOUBLE>'");

        assertParsingError("object * decimal1", "Cannot apply '*' to arguments of type '<OBJECT> * <DECIMAL(38, 38)>'");
        assertParsingError("object * bigInteger1", "Cannot apply '*' to arguments of type '<OBJECT> * <DECIMAL(38, 38)>'");

        assertParsingError("object * string1", "Cannot apply '*' to arguments of type '<OBJECT> * <VARCHAR>'");
        assertParsingError("object * char1", "Cannot apply '*' to arguments of type '<OBJECT> * <VARCHAR>'");

        assertParsingError("object * object", "Cannot apply '*' to arguments of type '<OBJECT> * <OBJECT>'");
    }

}
