package guido.network;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import guido.core.databasis.IDataBasisElement;
import guido.core.proof.controller.IProofControl;
import guido.core.verifier.AVerificationSystemFactory;
import guido.core.verifier.IJob;
import guido.key.pooling.distributed.ResultRunnable;
import guido.network.client.FileClient;

public class ProofRunnable implements ResultRunnable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6790934214721419957L;

	private final int fileServerPort;
	private final IJob job;
	private Object result;
	private transient String ip;
	public static String mode;

	public ProofRunnable(IJob job, int fileServerPort) {
		super();
		this.job = job;
		this.fileServerPort = fileServerPort;
	}

	@Override
	public void run() {
		try {
			File localTemp = new File(new File("Temp"), "Client");
			localTemp.mkdirs();

			System.out.println("Running: " + job);
			// here Factory adding
			IProofControl kc = AVerificationSystemFactory.getFactory().createProofControl();
			FileClient fc = new FileClient(ip, fileServerPort);

			// File sourceFile = getFileForName(fc, source, localTemp);
			// File classpathFile = getFileForName(fc, classpath, localTemp);

			List<IDataBasisElement> intermediate = new ArrayList<IDataBasisElement>();
			intermediate.add(kc.performProof(job));

			for (IDataBasisElement result : intermediate) {
				// result.setCode(job.getCode());
				// result.setExperiments(job.getExperiments());
			}
			ResultCommunication resultComm = new ResultCommunication(job, intermediate);
			result = resultComm;
		} catch (Exception e) {
			result = e;
		}
	}

	private static File getFileForName(FileClient fc, String name, File temp)
			throws ClassNotFoundException, IOException {
		if (name == null)
			return null;

		String localSourceName = fc.getFilename(name);
		File localSourceFile = new File(temp, localSourceName);
		if (!localSourceFile.exists())
			fc.getFile(localSourceName, temp);
		return localSourceFile;
	}

	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public void setIPAdress(String ip) {
		this.ip = ip;
	}

}
