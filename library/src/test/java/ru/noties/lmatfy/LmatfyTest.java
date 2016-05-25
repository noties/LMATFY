package ru.noties.lmatfy;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Dimitry Ivanov on 25.05.2016.
 */
public class LmatfyTest {

    interface Impl1 {}
    interface Impl2 {}

    @Test
    public void nulls() {
        try {
            Lmatfy.please(null, null);
            Lmatfy.please(null, new Object());
            Lmatfy.please(new Object(), null);
            assertTrue(false);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void noFields() {
        try {
            Lmatfy.please(new Object(), new Object());
            assertTrue(false);
        } catch (AttachException e) {
            assertTrue(true);
        }
    }

    @Test
    public void singleField() {
        final class Holder {
            @Attach
            Impl1 mImpl1;
        }
        final class Target implements Impl1 {}

        final Holder holder = new Holder();
        final Target target = new Target();

        Lmatfy.please(holder, target);

        assertTrue(holder.mImpl1 == target);
    }

    @Test
    public void multipleFields() {
        final class Holder {
            @Attach
            Impl1 mImpl1;

            @Attach
            Impl2 mImpl2;
        }

        final class Target implements Impl1, Impl2 {}

        final Holder holder = new Holder();
        final Target target = new Target();

        Lmatfy.please(holder, target);

        assertTrue(holder.mImpl1 == target);
        assertTrue(holder.mImpl2 == target);
    }

    @Test
    public void superFields() {
        class HolderBase {
            @Attach
            Impl1 mImpl1;
        }
        final class Holder extends HolderBase {
            @Attach
            Impl2 mImpl2;
        }
        final class Target implements Impl1, Impl2 {}

        final Holder holder = new Holder();
        final Target target = new Target();

        Lmatfy.please(holder, target);

        assertTrue(holder.mImpl1 == target);
        assertTrue(holder.mImpl2 == target);
    }

    @Test
    public void attachSelf() {
        final class HolderTarget implements Impl1 {
            @Attach
            private Impl1 mSelf;
        }

        final HolderTarget holderTarget = new HolderTarget();

        Lmatfy.please(holderTarget, holderTarget);

        assertTrue(holderTarget == holderTarget.mSelf);
    }

    @Test
    public void cannotAttach() {
        final class Holder {
            @Attach
            Impl1 mImpl1;
        }

        final Holder holder = new Holder();

        try {
            Lmatfy.please(holder, new Object());
            assertTrue(false);
        } catch (AttachException e) {
            assertTrue(true);
        }
    }
}
