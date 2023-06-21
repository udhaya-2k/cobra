package com.quali.cobra;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class App {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String host = "Ubuntu";
        String user = "tarus";
        String password = "tarus";
        String localpath = System.getProperty("user.dir") + "/cpuload.py";
        String sftpPath = "/home/tarus/Downloads/cpuload.py";
        String command = "cd /home/tarus/Downloads && python3 cpuload.py";
        int port = 22;

        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println("Connected to the Endpoint");

            // File Transfer
            Channel channel2 = session.openChannel("sftp");
            ChannelSftp sftpChannel = (ChannelSftp) channel2;
            sftpChannel.connect(60000);
            sftpChannel.put(localpath, sftpPath);
            sftpChannel.disconnect();
            System.out.println("File Transfer Completed");

            // Command Execution
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            channel.disconnect();
            System.out.println("Done Executing Command");
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
