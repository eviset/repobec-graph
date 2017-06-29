package pro.filatov.workstation4ceb.model.uart;

import com.ftdichip.usb.FTDI;
import com.ftdichip.usb.FTDIUtility;
import com.ftdichip.usb.enumerated.EFlowControl;
import com.ftdichip.usb.enumerated.ELineDatabits;
import com.ftdichip.usb.enumerated.ELineParity;
import com.ftdichip.usb.enumerated.ELineStopbits;
import org.usb4java.Context;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import pro.filatov.workstation4ceb.form.editor.ILongProcessEventListener;

import javax.usb3.IUsbDevice;
import javax.usb3.exception.UsbDisconnectedException;
import javax.usb3.exception.UsbException;
import javax.usb3.ri.UsbDevice;
import javax.usb3.ri.UsbDeviceId;
import javax.usb3.utility.JNINativeLibraryLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yuri.filatov on 01.08.2016.
 */
public class UartModel {


    private int baudRate = 1000000;
    private byte[] packetForSending;
    private byte[] packetReceived;

    private boolean isProgramming;
    private IUsbDevice curUsbDevice;

    List<ILongProcessEventListener> listLongProcessEventListeners = new ArrayList<>();

    public boolean isProgramming() {
        return isProgramming;
    }

    public void setProgramming(boolean programming) {
        isProgramming = programming;
        for(ILongProcessEventListener listener: listLongProcessEventListeners){
            listener.updateStatusOfProgramming();
        }
    }



    FTDI ftdiDevice;
    public EchangePacketAction doExchangePacket(byte[] packet){
        packetForSending = packet;
        EchangePacketAction t = new EchangePacketAction();
        t.start();
        return t;
    }


    public void reOpenFTDI(){
        try {
            Thread t = new OpenFtdiAction();
            t.start();
            t.join(5000);
            if (ftdiDevice == null) {
                System.out.println("Ftdi device not configured.");
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public byte [] getResponse(){
            return packetReceived;
    }






     class EchangePacketAction extends Thread {

        private int numSentBytes;

        @Override
        public void run() {

                try {
                    if(ftdiDevice == null){
                        openFTDI();
                    }else if(curUsbDevice.isConnected()){

                    }
                    boolean flag = true;
                    System.out.println("PC->CEB: " + PacketHelper.convPacketToHexString(packetForSending, " "));
                    packetReceived = null;
                    byte[]response = null;
                    while(flag) {
                        ftdiDevice.write(packetForSending);
                        sleep(20);
                        response = ftdiDevice.read();
                        if(response == null | response.length == 0){
                            System.out.println("No response from CEB (BOX)! ");
                            break;
                        }else if (response.length > 2 && response[1] == (byte) 0xFF) {
                            System.out.println("Small packet from PC. Retry.");
                        } else {
                            System.out.println("CEB->PC: " +PacketHelper.convPacketToHexString(response, " "));
                            flag = false;
                            break;
                        }
                    }
                    packetReceived = response;
                }catch (UsbDisconnectedException e){
                    openFTDI();
                }
                catch (UsbException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }


         private void openFTDI(){
             try {
                     Thread t = new OpenFtdiAction();
                     t.start();
                    int i = 1;
                    while (i > 0){
                        i--;
                    }
                     t.join(5000);
                     if (ftdiDevice == null) {
                         System.out.println("Ftdi device not configured.");
                         return;
                     }
             }catch (InterruptedException e) {
                 e.printStackTrace();
             }


         }


        public int getNumSentBytes() {
            return numSentBytes;
        }
    }


    private class OpenFtdiAction extends Thread {
        @Override
        public void run() {

            try {

                System.out.println("Initialization FTDI device...");
                JNINativeLibraryLoader.load();
                Context jniContext = new Context();
                final int init = LibUsb.init(jniContext);

                final List<UsbDeviceId> current = new ArrayList<>();

                final DeviceList deviceList = new DeviceList();
                Collection<IUsbDevice> devices = FTDIUtility.findFTDIDevices();
                sleep(500);

                if (devices.size() == 0) {
                    System.out.println("Not found any FTDI device!");
                } else {
                    for (IUsbDevice iUsbDevice : devices) {
                        System.out.println("Found FTDI device:  " + iUsbDevice);
                    }
                }
                IUsbDevice usbDevice = devices.iterator().next();
                sleep(500);
                if(curUsbDevice == null) {
                    curUsbDevice = usbDevice;
                    ftdiDevice = FTDI.getInstance(usbDevice);
                }else {
                    try{
                        curUsbDevice.isConnected();
                    }catch(UsbDisconnectedException e){
                            curUsbDevice = usbDevice;
                            ftdiDevice = FTDI.getInstance(usbDevice);
                    }
                 }

                System.out.println("Configuring FTDI....\n BaudRate = " + baudRate + " bit/sec");
                ftdiDevice.configureSerialPort(baudRate, ELineDatabits.BITS_8, ELineStopbits.STOP_BIT_1, ELineParity.NONE, EFlowControl.DISABLE_FLOW_CTRL);

            } catch (UsbDisconnectedException e){
                System.out.println("FTDI from USB is  diconnected!");
            }
            catch (UsbException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }



    public void addLongProcessEventListener(ILongProcessEventListener listener){
        listLongProcessEventListeners.add(listener);
    }
}
