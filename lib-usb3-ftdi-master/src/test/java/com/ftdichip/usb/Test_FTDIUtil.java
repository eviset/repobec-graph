/*
 * Copyright (C) 2014 Jesse Caulfield .
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package com.ftdichip.usb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.usb3.IUsbDevice;
import javax.usb3.exception.UsbException;
import javax.usb3.ri.UsbDeviceId;
import javax.usb3.utility.ByteUtility;
import javax.usb3.utility.JNINativeLibraryLoader;

import com.ftdichip.usb.enumerated.EFlowControl;
import com.ftdichip.usb.enumerated.ELineDatabits;
import com.ftdichip.usb.enumerated.ELineParity;
import com.ftdichip.usb.enumerated.ELineStopbits;
import org.junit.Test;
import org.usb4java.Context;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;

/**
 *
 * @author Jesse Caulfield
 */
public class Test_FTDIUtil {

  @Test
  public void testFTDI() throws UsbException {

    JNINativeLibraryLoader.load();
    Context jniContext = new Context();
    final int init = LibUsb.init(jniContext);

    final List<UsbDeviceId> current = new ArrayList<>();

    // Get device list from libusb and abort if it failed
    final DeviceList deviceList = new DeviceList();
    Collection<IUsbDevice> devices = FTDIUtility.findFTDIDevices();
    for (IUsbDevice iUsbDevice : devices) {
      System.out.println("FOUND FTDI device:  " + iUsbDevice);
    }

    IUsbDevice usbDevice = devices.iterator().next();
    FTDI ftdiDevice = FTDI.getInstance(usbDevice);
    // Read data from the FTDI device output buffer
    System.out.println("Read any data from the FTDI device output buffer");
    byte[]data = {34, 23, 34};

    ftdiDevice.configureSerialPort(9600, ELineDatabits.BITS_8, ELineStopbits.STOP_BIT_1, ELineParity.NONE, EFlowControl.DTR_DSR_HS);
    for (int i = 0; i<10; i++) {
      ftdiDevice.write(data);

    }
    byte[] usbFrame = ftdiDevice.read();
    while (usbFrame.length > 0) {
      System.out.println("   READ " + usbFrame.length + " bytes: " + ByteUtility.toString(usbFrame));
      usbFrame = ftdiDevice.read();
    }

  }
}
