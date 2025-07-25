---
navigation:
  title: "パイプ"
  icon: "modern_industrialization:wrench"
  position: 4
  parent: modern_industrialization:steam_age.md
---

# パイプ

パイプは一部の機械のレシピに使用されているが、ワールドに設置することも可能だ。1つのブロックに最大3本のパイプを設置することができる。

色の違うパイプは繋がらない。

レンチでパイプを右クリックすると接続の種類が変わり、シフトを押しながら右クリックするとパイプがドロップする。

## 流体パイプ

<ItemImage id="modern_industrialization:fluid_pipe" />

流体パイプには1種類の流体しか入れることができず、デフォルトでは空のパイプやブロックとは接続しない。パイプの中央をスパナで右クリックするか、パイプを手に持った状態でブロックを右クリックすれば、強制的に接続させることができる。このとき、手に持っているアイテムは消費されない。

## アイテムパイプ

<ItemImage id="modern_industrialization:item_pipe" />

アイテムパイプはデフォルトではインベントリーに接続しないが、強制的に接続させることができる。

レンチを押さずに接続部を右クリックするとフィルターが表示される。デフォルトではホワイトリストモードが有効になっているため、アイテムが直接搬入または搬出されることはない。

パイプの種類によって、どのように機能するかは異なります。

Tickごとに流体パイプはすべての接続されたブロックに対して均等に搬出しようとし、次に均等に搬出しようとする。[ 速度の詳細については、こちらを参照。](../midgame/fluid_transfer.md)

アイテムパイプは優先順位の高いものから順番にアイテムを投入し、数秒ごとに16個のアイテムを転送する。**モーターでアップグレードできる。**

## 備考

アイテムパイプはデフォルトでホワイトリストになっている(つまり、フィルター内のアイテムだけが搬入出される)。 ブラックリスト(フィルターにないアイテムだけが搬入出される)に設定するか、搬入出させたいアイテムをフィルターに追加する必要がある。パイプの両側を設定しないとアイテムが移動しないことに注意。

