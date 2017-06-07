package net.cakesolutions.strictify.core

trait StrictifySeqsImplicits extends StrictifyInstanceHelpers {
  implicit def strictifySeqItems[S, L](
    implicit itemInstance: Strictify[S, L]
  ) =
    instance[Seq[S], Seq[L]](
      _.map(itemInstance.apply)
        .foldRight[Either[StrictifyError, Seq[S]]](Right(Seq.empty)) {
          case (_, Left(e))                => Left(e)
          case (Left(e), _)                => Left(e)
          case (Right(item), Right(items)) => Right(item +: items)
        }
    )
}

trait LoosenSeqsImplicits extends LoosenInstanceHelpers {
  implicit def loosenSToeqItems[S, L](implicit itemInstance: Loosen[S, L]) =
    instance[Seq[S], Seq[L]](_.map(itemInstance.apply))
}

trait BindingSeqsImplicits
    extends StrictifySeqsImplicits
    with LoosenSeqsImplicits
