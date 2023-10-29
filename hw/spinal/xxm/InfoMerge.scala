package xxm

import spinal.core._
import spinal.lib._

case class InfoMerge() extends Component {
    val io = new Bundle {
        val memRd = slave(Stream(UInt(8 bits)))
        val extraInfo = slave(Stream(UInt(8 bits)))
        val resultInfo  = master(Stream(UInt(40 bits)))
    }

    noIoPrefix()

    val mem = Mem(Bits(32 bits), 1 << 8)

    val resultRd = mem.streamReadSync(io.memRd)

    val tmp_extraInfo = io.extraInfo.m2sPipe().s2mPipe()

    StreamJoin.arg(tmp_extraInfo, resultRd).translateWith((tmp_extraInfo.payload ## resultRd.payload).asUInt) >/-> io.resultInfo

}

object RunInfoMerge extends App {
    MyConfig.spinal.generateSystemVerilog(InfoMerge())
}
