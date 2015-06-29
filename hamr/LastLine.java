

package org.cisc475.shortestpath.hamr;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class LastLine {
	private int max;
	private File file;

	public static  int Maximum(String filepath){
		File file= new File(filepath);
		RandomAccessFile fileHandler = null;
		try {
			fileHandler = new RandomAccessFile( file, "r" );
			long fileLength = fileHandler.length() - 1;
			StringBuilder sb = new StringBuilder();

			for(long filePointer = fileLength; filePointer != -1; filePointer--){
				fileHandler.seek( filePointer );
				int readByte = fileHandler.readByte();

				if( readByte == 0xA ) {
					if( filePointer == fileLength ) {
						continue;
					}
					break;

				} else if( readByte == 0xD ) {
					if( filePointer == fileLength - 1 ) {
						continue;
					}
					break;
				}

				sb.append( ( char ) readByte );
			}

			String lastLine = sb.reverse().toString();
			String[] temp = lastLine.split(" ");
			Integer Maximum = Integer.parseInt(temp[0]); 
			return Maximum;
    } catch( java.io.FileNotFoundException e ) {
        e.printStackTrace();
        return 0;
    } catch( java.io.IOException e ) {
        e.printStackTrace();
        return 0;
    } finally {
        if (fileHandler != null )
            try {
                fileHandler.close();
            } catch (IOException e) {
                /* ignore */
            }
    }
}
	

}
