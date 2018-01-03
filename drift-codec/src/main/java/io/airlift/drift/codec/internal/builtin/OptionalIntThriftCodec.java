/*
 * Copyright (C) 2012 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.airlift.drift.codec.internal.builtin;

import io.airlift.drift.codec.ThriftCodec;
import io.airlift.drift.codec.metadata.ThriftType;
import io.airlift.drift.protocol.TProtocolReader;
import io.airlift.drift.protocol.TProtocolWriter;

import javax.annotation.concurrent.Immutable;

import java.util.OptionalInt;

import static java.util.Objects.requireNonNull;

@Immutable
public class OptionalIntThriftCodec
        implements ThriftCodec<OptionalInt>
{
    @Override
    public ThriftType getType()
    {
        return new ThriftType(ThriftType.I32, OptionalInt.class, OptionalInt.empty());
    }

    @Override
    public OptionalInt read(TProtocolReader protocol)
            throws Exception
    {
        requireNonNull(protocol, "protocol is null");
        return OptionalInt.of(protocol.readI32());
    }

    @Override
    public void write(OptionalInt value, TProtocolWriter protocol)
            throws Exception
    {
        requireNonNull(value, "value is null");
        requireNonNull(protocol, "protocol is null");

        // write can not be called with a missing value, and instead the write should be skipped
        // after check the result from isNull
        protocol.writeI32(value.orElseThrow(() -> new IllegalArgumentException("value is not present")));
    }

    @Override
    public boolean isNull(OptionalInt value)
    {
        return value == null || !value.isPresent();
    }
}
