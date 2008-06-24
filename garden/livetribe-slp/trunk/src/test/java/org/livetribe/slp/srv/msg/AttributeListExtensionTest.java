/*
 * Copyright 2006 the original author or authors
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
package org.livetribe.slp.srv.msg;

import org.livetribe.slp.Attributes;
import org.testng.annotations.Test;

/**
 * @version $Rev: 157 $ $Date: 2006-06-05 23:29:25 +0200 (Mon, 05 Jun 2006) $
 */
public class AttributeListExtensionTest
{
    @Test
    public void testAttributeListExtension() throws Exception
    {
        AttributeListExtension original = new AttributeListExtension();
        original.setURL("service:foo:bar://baz");
        Attributes originalAttributes = Attributes.from("(attr=value)");
        original.setAttributes(originalAttributes);
        // TODO: test auth blocks

        byte[] bytes = original.serialize();
        AttributeListExtension deserialized = (AttributeListExtension)Extension.deserialize(bytes);

        assert original.getURL().equals(deserialized.getURL());
        assert originalAttributes.equals(deserialized.getAttributes());
        // TODO: test auth blocks
    }
}
