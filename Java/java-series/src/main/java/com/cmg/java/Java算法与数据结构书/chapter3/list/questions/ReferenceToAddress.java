//package com.cmg.java.algorithmBook.list.questions;
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
//public class ReferenceToAddress {
//    private static final Unsafe unsafe = getUnsafe();
//    private static final boolean is64bit = true;
//
//
//    private static Unsafe getUnsafe() {
//        try {
//            Field field = Unsafe.class.getDeclaredField("theUnsafe");
//            field.setAccessible(true);
//            return (Unsafe) field.get(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
////    public static void printAddresses(String label, Object... objects) {
////        System.out.print(label + ": 0x");
////        long last = 0;
////        int offset = unsafe.arrayBaseOffset(objects.getClass());
////        int scale = unsafe.arrayIndexScale(objects.getClass());
////        switch (scale) {
////            case 4:
////                long factor = is64bit ? 8 : 1;
////                final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
////                System.out.print(Long.toHexString(i1));
////                last = i1;
////                for (int i = 1; i < objects.length; i++) {
////                    final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
////                    if (i2 > last)
////                        System.out.print(", +" + Long.toHexString(i2 - last));
////                    else
////                        System.out.print(", -" + Long.toHexString(last - i2));
////                    last = i2;
////                }
////                break;
////            case 8:
////                throw new AssertionError("Not supported");
////        }
////        System.out.println();
////    }
//
//    public static long addressOf(Object o) throws Exception {
//        Object[] array = new Object[]{o};
//        //first obj address' offset
//        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
//        int addressSize = unsafe.addressSize();
//        long objectAddress;
//        switch (addressSize) {
//            case 4:
//                objectAddress = unsafe.getInt(array, baseOffset);
//                break;
//            case 8:
//                objectAddress = unsafe.getLong(array, baseOffset);
//                break;
//            default:
//                throw new Error("unsupported address size: " + addressSize);
//        }
//        return (objectAddress);
//    }
//
//}
