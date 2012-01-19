/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cyrille.util.concurrent;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;

import com.google.common.util.concurrent.Futures;

/**
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class ExceptionFutureTest {

    @Test
    public void testGetWithExecutionException() throws InterruptedException {
        ExecutionException expected = new ExecutionException("My Execution Exception", null);
        Future<Object> exceptionFuture = Futures.immediateFailedFuture(expected);
        try {
            Object object = exceptionFuture.get();
            fail("An exception should have occur : " + object);
        } catch (ExecutionException actual) {
            // ok
        }
    }

    @Test
    public void testGetWithThrowable() throws InterruptedException {
        Throwable expected = new IOException("My IOException");
        Future<Object> exceptionFuture = Futures.immediateFailedFuture(expected);
        try {
            Object object = exceptionFuture.get();
            fail("An exception should have occur : " + object);
        } catch (ExecutionException actual) {
            assertSame(expected, actual.getCause());
        }
    }

}
