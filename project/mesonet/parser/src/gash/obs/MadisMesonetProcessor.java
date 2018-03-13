package gash.obs.madis;

import java.io.File;
import java.io.FileFilter;

import gash.obs.ObsData;
import gash.obs.Station;
import gash.obs.ShardingAlgo;
import gash.obs.Store;
import gash.obs.TimeSharding;

public class MadisMesonetProcessor {

	/** raw data file naming convention */
	private static final String sDataTag = null;

	/** file name prefix for general data */
	public static final String sGeneralTag = "madis-mesonet";

	public MadisMesonetProcessor() {
		super();
		init(Store.StoreAs.CSV, null); // TODO incomplete construction
	}

	public MadisMesonetProcessor(File catalogF, Store.StoreAs dataAs, File outputDir) {
		super(catalogF, dataAs, outputDir);
		init(dataAs, outputDir);
	}

	private void init(Store.StoreAs dataAs, File outputDir) {
		ShardingAlgo fs = new TimeSharding(Store.StoreAs.CSV, outputDir, sGeneralTag);
		this.setShardAlgo(fs);
	}

	protected ObsData newObsData() {
		ObsData rtn = new ObsData();
		rtn.addMetadata("source", sDataTag);
		rtn.addMetadata("type", Station.StationType.Surface.toString());

		return rtn;
	}

	/**
	 * assumption: data is received once every 15 minutes or so.
	 * 
	 * This will perform batch processing.
	 * 
	 * @param args
	 */
	public static final void main(String[] args) {
		if (args.length != 4) {
			System.exit(2);
		}

		File rawdataDir = new File(args[0]);
		File catF = new File(args[2]);
		File outdir = new File(args[3]);

		long startTime = System.currentTimeMillis();
		try {
			MadisMesonetProcessor processor = new MadisMesonetProcessor(catF, Store.StoreAs.CSV,
					outdir);

			// need to supply the native data reader (this converts native data
			// to the general format). Note this is reused for each file to
			// process.
			MadisMesonetReader rawReader = new MadisMesonetReader();
			processor.setRawReader(rawReader);

			if (rawdataDir.exists()) {
				FileFilter filter = new FileFilter() {
					public boolean accept(File pathname) {
						// data should already be unzipped by scripts
						return (pathname.isFile() && !pathname.getName().startsWith(".") && !pathname
								.getName().endsWith(".gz"));
					}
				};

				processor.processDir(rawdataDir, filter);
			}

			long stopTime = System.currentTimeMillis();

			ObsProcessor.log.debug("MADIS Mesonet - total processing time is "
					+ ((stopTime - startTime) / 1000.0) + " seconds");
		} catch (Throwable t) {
			ObsProcessor.log.error(
					"Unable to process mesowest data in " + rawdataDir.getAbsolutePath(), t);
		}
	}
}
