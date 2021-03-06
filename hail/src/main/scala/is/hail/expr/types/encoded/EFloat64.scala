package is.hail.expr.types.encoded

import is.hail.annotations.Region
import is.hail.asm4s._
import is.hail.expr.types.BaseType
import is.hail.expr.types.physical._
import is.hail.expr.types.virtual._
import is.hail.io.{InputBuffer, OutputBuffer}
import is.hail.utils._

case object EFloat64Optional extends EFloat64(false)
case object EFloat64Required extends EFloat64(true)

class EFloat64(override val required: Boolean) extends EType {
  def _buildEncoder(pt: PType, mb: MethodBuilder, v: Value[_], out: Value[OutputBuffer]): Code[Unit] = {
    out.writeDouble(coerce[Double](v))
  }

  def _buildDecoder(
    pt: PType,
    mb: MethodBuilder,
    region: Code[Region],
    in: Code[InputBuffer]
  ): Code[Double] = in.readDouble()

  def _buildSkip(mb: MethodBuilder, r: Code[Region], in: Code[InputBuffer]): Code[Unit] = in.skipDouble()

  override def _compatible(pt: PType): Boolean = pt.isInstanceOf[PFloat64]

  def _decodedPType(requestedType: Type): PType = PFloat64(required)

  def _asIdent = "float64"
  def _toPretty = "EFloat64"
}

object EFloat64 {
  def apply(required: Boolean = false): EFloat64 = if (required) EFloat64Required else EFloat64Optional
}
