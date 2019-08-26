package org.lasque.tusdk.core.media.codec.extend;

import java.util.Arrays;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkH264SPS
{
  public final byte[] data;
  public boolean isSps;
  public int width;
  public int height;
  public int fps;
  public int dar_width;
  public int dar_height;
  public int forbidden_zero_bit = 0;
  public int nal_ref_idc = 0;
  public int nal_unit_type = 0;
  public int profile_idc = 0;
  public int constraint_set0_flag = 0;
  public int constraint_set1_flag = 0;
  public int constraint_set2_flag = 0;
  public int constraint_set3_flag = 0;
  public int reserved_zero_4bits = 0;
  public int level_idc = 0;
  public int seq_parameter_set_id = 0;
  public int chroma_format_idc = 0;
  public int residual_colour_transform_flag = 0;
  public int bit_depth_luma_minus8 = 0;
  public int bit_depth_chroma_minus8 = 0;
  public int qpprime_y_zero_transform_bypass_flag = 0;
  public int seq_scaling_matrix_present_flag = 0;
  public int[] seq_scaling_list_present_flag;
  public int log2_max_frame_num_minus4 = 0;
  public int pic_order_cnt_type = 0;
  public int log2_max_pic_order_cnt_lsb_minus4 = 0;
  public int delta_pic_order_always_zero_flag = 0;
  public int offset_for_non_ref_pic = 0;
  public int offset_for_top_to_bottom_field = 0;
  public int num_ref_frames_in_pic_order_cnt_cycle = 0;
  public int num_ref_frames = 0;
  public int gaps_in_frame_num_value_allowed_flag = 0;
  public int pic_width_in_mbs_minus1 = 0;
  public int pic_height_in_map_units_minus1 = 0;
  public int frame_mbs_only_flag = 0;
  public int mb_adaptive_frame_field_flag = 0;
  public int direct_8x8_inference_flag = 0;
  public int frame_cropping_flag = 0;
  public int frame_crop_left_offset = 0;
  public int frame_crop_right_offset = 0;
  public int frame_crop_top_offset = 0;
  public int frame_crop_bottom_offset = 0;
  public int vui_parameters_present_flag = 0;
  public int aspect_ratio_info_present_flag = 0;
  public int aspect_ratio_idc = 0;
  public int sar_width = 0;
  public int sar_height = 0;
  public int overscan_info_present_flag = 0;
  public int overscan_appropriate_flag = 0;
  public int video_signal_type_present_flag = 0;
  public int video_format = 0;
  public int video_full_range_flag = 0;
  public int colour_description_present_flag = 0;
  public int colour_primaries = 0;
  public int transfer_characteristics = 0;
  public int matrix_coefficients = 0;
  public int chroma_loc_info_present_flag = 0;
  public int chroma_sample_loc_type_top_field = 0;
  public int chroma_sample_loc_type_bottom_field = 0;
  public int timing_info_present_flag = 0;
  public int num_units_in_tick = 0;
  public int time_scale = 0;
  public int fixed_frame_rate_flag = 0;
  public int nal_hrd_parameters_present_flag = 0;
  public int vcl_hrd_parameters_present_flag = 0;
  public int low_delay_hrd_flag = 0;
  public int pic_struct_present_flag = 0;
  public int bitstream_restriction_flag = 0;
  public int motion_vectors_over_pic_boundaries_flag = 0;
  public int max_bytes_per_pic_denom = 0;
  public int max_bits_per_mb_denom = 0;
  public int log2_max_mv_length_horizontal = 0;
  public int log2_max_mv_length_vertical = 0;
  public int num_reorder_frames = 0;
  public int max_dec_frame_buffering = 0;
  public int cpb_cnt_minus1 = 0;
  public int bit_rate_scale = 0;
  public int cpb_size_scale = 0;
  public int initial_cpb_removal_delay_length_minus1 = 0;
  public int cpb_removal_delay_length_minus1 = 0;
  public int dpb_output_delay_length_minus1 = 0;
  public int time_offset_length = 0;
  private static final int[][] a = { { 0, 1 }, { 1, 1 }, { 12, 11 }, { 10, 11 }, { 16, 11 }, { 40, 33 }, { 24, 11 }, { 20, 11 }, { 32, 11 }, { 80, 33 }, { 18, 11 }, { 15, 11 }, { 64, 33 }, { 160, 99 }, { 4, 3 }, { 3, 2 }, { 2, 1 } };
  private int b = 32;
  
  public TuSdkH264SPS(byte[] paramArrayOfByte)
  {
    this.data = paramArrayOfByte;
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length < 11) || (paramArrayOfByte[0] != 0) || (paramArrayOfByte[1] != 0) || (paramArrayOfByte[2] != 0) || (paramArrayOfByte[3] != 1))
    {
      TLog.w("%s null sps info.", new Object[] { "TuSdkH264SPS" });
      return;
    }
    a();
  }
  
  private void a()
  {
    b(this.data);
    this.forbidden_zero_bit = a(this.data, 1);
    this.nal_ref_idc = a(this.data, 2);
    this.nal_unit_type = a(this.data, 5);
    if (this.nal_unit_type != 7) {
      return;
    }
    this.isSps = true;
    this.profile_idc = a(this.data, 8);
    this.constraint_set0_flag = a(this.data, 1);
    this.constraint_set1_flag = a(this.data, 1);
    this.constraint_set2_flag = a(this.data, 1);
    this.constraint_set3_flag = a(this.data, 1);
    this.reserved_zero_4bits = a(this.data, 4);
    this.level_idc = a(this.data, 8);
    this.seq_parameter_set_id = a(this.data);
    if ((this.profile_idc == 100) || (this.profile_idc == 110) || (this.profile_idc == 122) || (this.profile_idc == 144))
    {
      this.chroma_format_idc = a(this.data);
      if (this.chroma_format_idc == 3) {
        this.residual_colour_transform_flag = a(this.data, 1);
      }
      this.bit_depth_luma_minus8 = a(this.data);
      this.bit_depth_chroma_minus8 = a(this.data);
      this.qpprime_y_zero_transform_bypass_flag = a(this.data, 1);
      this.seq_scaling_matrix_present_flag = a(this.data, 1);
      this.seq_scaling_list_present_flag = new int[8];
      if (this.seq_scaling_matrix_present_flag != 0) {
        for (int i = 0; i < 8; i++) {
          this.seq_scaling_list_present_flag[i] = a(this.data, 1);
        }
      }
    }
    this.log2_max_frame_num_minus4 = a(this.data);
    this.pic_order_cnt_type = a(this.data);
    int[] arrayOfInt;
    int k;
    if (this.pic_order_cnt_type == 0)
    {
      this.log2_max_pic_order_cnt_lsb_minus4 = a(this.data);
    }
    else if (this.pic_order_cnt_type == 1)
    {
      this.delta_pic_order_always_zero_flag = a(this.data, 1);
      this.offset_for_non_ref_pic = c(this.data, -1);
      this.offset_for_top_to_bottom_field = c(this.data, -1);
      this.num_ref_frames_in_pic_order_cnt_cycle = a(this.data);
      arrayOfInt = new int[this.num_ref_frames_in_pic_order_cnt_cycle];
      for (k = 0; k < this.num_ref_frames_in_pic_order_cnt_cycle; k++) {
        arrayOfInt[k] = c(this.data, -1);
      }
    }
    this.num_ref_frames = a(this.data);
    this.gaps_in_frame_num_value_allowed_flag = a(this.data, 1);
    this.pic_width_in_mbs_minus1 = a(this.data);
    this.pic_height_in_map_units_minus1 = a(this.data);
    this.width = ((this.pic_width_in_mbs_minus1 + 1) * 16);
    this.height = ((this.pic_height_in_map_units_minus1 + 1) * 16);
    this.frame_mbs_only_flag = a(this.data, 1);
    if (this.frame_mbs_only_flag == 0) {
      this.mb_adaptive_frame_field_flag = a(this.data, 1);
    }
    this.direct_8x8_inference_flag = a(this.data, 1);
    this.frame_cropping_flag = a(this.data, 1);
    if (this.frame_cropping_flag != 0)
    {
      this.frame_crop_left_offset = a(this.data);
      this.frame_crop_right_offset = a(this.data);
      this.frame_crop_top_offset = a(this.data);
      this.frame_crop_bottom_offset = a(this.data);
    }
    this.vui_parameters_present_flag = a(this.data, 1);
    if (this.vui_parameters_present_flag != 0)
    {
      this.aspect_ratio_info_present_flag = a(this.data, 1);
      if (this.aspect_ratio_info_present_flag != 0)
      {
        this.aspect_ratio_idc = a(this.data, 8);
        if (this.aspect_ratio_idc == 255)
        {
          this.sar_width = a(this.data, 16);
          this.sar_height = a(this.data, 16);
        }
        else if (this.aspect_ratio_idc < a.length)
        {
          arrayOfInt = a[this.aspect_ratio_idc];
          this.sar_width = arrayOfInt[0];
          this.sar_height = arrayOfInt[1];
        }
        if ((this.sar_width > 0) && (this.sar_height > 0) && (this.width > 0) && (this.height > 0))
        {
          int j = this.width - this.frame_crop_left_offset * 2 - this.frame_crop_right_offset * 2;
          k = this.height - this.frame_crop_top_offset * 2 - this.frame_crop_bottom_offset * 2;
          if (j > k)
          {
            this.dar_width = j;
            this.dar_height = (k * this.sar_height / this.sar_width);
          }
          else
          {
            this.dar_width = (j * this.sar_width / this.sar_height);
            this.dar_height = k;
          }
        }
      }
      this.overscan_info_present_flag = a(this.data, 1);
      if (this.overscan_info_present_flag != 0) {
        this.overscan_appropriate_flag = a(this.data, 1);
      }
      this.video_signal_type_present_flag = a(this.data, 1);
      if (this.video_signal_type_present_flag != 0)
      {
        this.video_format = a(this.data, 3);
        this.video_full_range_flag = a(this.data, 1);
        this.colour_description_present_flag = a(this.data, 1);
        if (this.colour_description_present_flag != 0)
        {
          this.colour_primaries = a(this.data, 8);
          this.transfer_characteristics = a(this.data, 8);
          this.matrix_coefficients = a(this.data, 8);
        }
      }
      this.chroma_loc_info_present_flag = a(this.data, 1);
      if (this.chroma_loc_info_present_flag != 0)
      {
        this.chroma_sample_loc_type_top_field = a(this.data);
        this.chroma_sample_loc_type_bottom_field = a(this.data);
      }
      this.timing_info_present_flag = a(this.data, 1);
      if (this.timing_info_present_flag != 0)
      {
        this.num_units_in_tick = a(this.data, 32);
        this.time_scale = a(this.data, 32);
        if ((this.num_units_in_tick > 0) && (this.time_scale > 0)) {
          this.fps = (this.time_scale / this.num_units_in_tick);
        }
        this.fixed_frame_rate_flag = a(this.data, 1);
        if (this.fixed_frame_rate_flag != 0) {
          this.fps /= 2;
        }
      }
    }
  }
  
  private int a(byte[] paramArrayOfByte, int paramInt)
  {
    return a(paramArrayOfByte, paramInt, -1);
  }
  
  private int a(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = paramInt2 == -1 ? this.b : paramInt2;
    for (int k = 0; k < paramInt1; k++)
    {
      i <<= 1;
      if ((paramArrayOfByte[(j / 8)] & 128 >> j % 8) != 0) {
        i++;
      }
      j++;
    }
    this.b = j;
    return i;
  }
  
  private int a(byte[] paramArrayOfByte)
  {
    return b(paramArrayOfByte, -1);
  }
  
  private int b(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    int j = -1;
    int k = paramInt == -1 ? this.b : paramInt;
    int m = 0;
    while (m != 1)
    {
      m = a(paramArrayOfByte, 1, k++);
      j++;
    }
    i = (int)(Math.pow(2.0D, j) - 1.0D + a(paramArrayOfByte, j, k));
    this.b = (k + j);
    return i;
  }
  
  private int c(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    int j = b(paramArrayOfByte, paramInt);
    i = (int)(Math.pow(-1.0D, j + 1) * Math.ceil(j / 2));
    return i;
  }
  
  private int b(byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    k = paramArrayOfByte.length;
    for (i = 0; i < k - 2; i++)
    {
      m = (paramArrayOfByte[i] ^ 0x0) + (paramArrayOfByte[(i + 1)] ^ 0x0) + (paramArrayOfByte[(i + 2)] ^ 0x3);
      if (m == 0)
      {
        for (j = i + 2; j < k - 1; j++) {
          paramArrayOfByte[j] = paramArrayOfByte[(j + 1)];
        }
        k--;
      }
    }
    return k;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkH264SPS").append("{ \n");
    localStringBuffer.append("isSps: ").append(this.isSps).append(", \n");
    localStringBuffer.append("nal_unit_type: ").append(this.nal_unit_type).append(", \n");
    localStringBuffer.append("profile_idc: ").append(this.profile_idc).append(", \n");
    localStringBuffer.append("level_idc: ").append(this.level_idc).append(", \n");
    localStringBuffer.append("width: ").append(this.width).append(", \n");
    localStringBuffer.append("height: ").append(this.height).append(", \n");
    localStringBuffer.append("sar_width: ").append(this.sar_width).append(", \n");
    localStringBuffer.append("sar_height: ").append(this.sar_height).append(", \n");
    localStringBuffer.append("dar_width: ").append(this.dar_width).append(", \n");
    localStringBuffer.append("dar_height: ").append(this.dar_height).append(", \n");
    localStringBuffer.append("frame_crop_left_offset: ").append(this.frame_crop_left_offset).append(", \n");
    localStringBuffer.append("frame_crop_right_offset: ").append(this.frame_crop_right_offset).append(", \n");
    localStringBuffer.append("frame_crop_top_offset: ").append(this.frame_crop_top_offset).append(", \n");
    localStringBuffer.append("frame_crop_bottom_offset: ").append(this.frame_crop_bottom_offset).append(", \n");
    localStringBuffer.append("num_units_in_tick: ").append(this.num_units_in_tick).append(", \n");
    localStringBuffer.append("time_scale: ").append(this.time_scale).append(", \n");
    localStringBuffer.append("fps: ").append(this.fps).append(", \n");
    localStringBuffer.append("fixed_frame_rate_flag: ").append(this.fixed_frame_rate_flag).append(", \n");
    if (this.data != null) {
      localStringBuffer.append("sps: ").append(Arrays.toString(this.data)).append(", \n");
    }
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkH264SPS.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */